using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Smartsense.Business.Abstract;
using Smartsense.Data.Abstract;
using Smartsense.Entity.DTO;
using Smartsense.Entity.Enums;
using Smartsense.Entity.Models;

namespace Smartsense.Business.Concrete
{
    public class DoctorService :IDoctorService
    {
        private readonly IDoctorRepository _doctorRepository;
        private readonly IValueRepository _valueRepository;
        private readonly IPatientRepository _patientRepository;
        private readonly IUserRepository _userRepository;
        private readonly IConnectionRepository _connectionRepository;
        private readonly ILocationRepository _locationRepository;
        private readonly IWarningRepository _warningRepository;
        private readonly ILocationWarningRepository _locationWarningRepository;
        public DoctorService(IDoctorRepository doctorRepository,IValueRepository valueRepository,ILocationWarningRepository locationWarningRepository, IWarningRepository warningRepository, IPatientRepository patientRepository,IUserRepository userRepository ,ILocationRepository locationRepository, IConnectionRepository connectionRepository)
        {
            _doctorRepository = doctorRepository;
            _valueRepository = valueRepository;
            _patientRepository = patientRepository;
            _userRepository = userRepository;
            _connectionRepository = connectionRepository;
            _locationRepository = locationRepository;
            _warningRepository = warningRepository;
            _locationWarningRepository = locationWarningRepository;
        }

        public DoctorGetPatientsResponse GetInfo(DoctorGetPatientsRequest request)
        {
            var response = new DoctorGetPatientsResponse();
           // var x= new DoctorGetPatientsResponse.PatientsData();
            var userid = request.UserId;

            var patients = _patientRepository.GetList(p => p.DoctorId == userid);

             if (patients == null)
             {
                 response.Code = "400";
                 response.Errors.Add("Hastalar bulunamadı.");
              return response;
             }

            var PatientsId = patients.Select(s => s.UserId).ToList();

           

            for (int i = 0; i < PatientsId.Count; i++)
            {
                var x = new DoctorGetPatientsResponse.PatientsData(); 
                var id = PatientsId[i]; 
                var patient = _patientRepository.Get(p => p.UserId == id );
                var PatientStatus = patient.UserStatus;
                var patientUser = _userRepository.Get(p=>p.Id==id);
                x.Name = patientUser.Name;
                x.Surname = patientUser.Surname;
                x.Diagnosis = patient.Diagnosis;
                x.BloodGroup = patient.BloodGroup;
                x.PatientStatus = patient.UserStatus;
               var patientValues = _valueRepository.GetList(p => p.UserId == id );

               
               var oksijen = patientValues.Where(w => w.DataType == DataValueType.OXYGEN).Select(s=>s.DataValue).LastOrDefault();
               var nabız = patientValues.Where(w => w.DataType == DataValueType.PULSE).Select(s => s.DataValue).LastOrDefault();
               var sıcaklık = patientValues.Where(w => w.DataType == DataValueType.TEMPERATURE).Select(s => s.DataValue).LastOrDefault();

               x.OxygenWarning = x.PulseWarning = x.TemperatureWarning = x.LocationWarning= ValueStatus.NORM;
               
                var location = _locationWarningRepository.GetList(p => p.UserId == id).LastOrDefault();
                if (location != null && location.CreatedDate>DateTime.Now.AddMinutes(-35))
                {
                    x.LocationWarning = ValueStatus.HIGH;
                }
                

               if (PatientStatus == UserStatus.QUARANTINE_AT_HOME)
               {

                   if (oksijen <= 92)
                   {
                       x.OxygenWarning = ValueStatus.LOW;
                   }

                   if (sıcaklık >= 38)
                   {
                       x.TemperatureWarning = ValueStatus.HIGH;
                   }

                   if (nabız >= 110)
                   {
                       x.PulseWarning = ValueStatus.HIGH;
                   }
                   if (nabız <= 50)
                   {
                       x.PulseWarning = ValueStatus.LOW;
                   }

                }
               if (PatientStatus == UserStatus.QUARANTINE_IN_HOSPITAL)
               {

                   if (oksijen <= 90)
                   {
                       x.OxygenWarning = ValueStatus.LOW;
                   }

                   if (sıcaklık >= 38.3)
                   {
                       x.TemperatureWarning = ValueStatus.HIGH;
                   }

                   if (nabız >= 110)
                   {
                       x.PulseWarning = ValueStatus.HIGH;
                   }
                   if (nabız <= 50)
                   {
                       x.PulseWarning = ValueStatus.LOW;
                   }

               }


               x.Id = id;
               x.Oxygen = oksijen;
               x.Pulse = nabız;
               x.Temperature = sıcaklık;

             

               response.patientsData.Add(x);

            }
            

            response.Message = "Hastalara ait veriler getirildi.";
            response.Code = "200";
            return response;


        }

        public PatientValueResponse GetPatientData(PatientValueRequest request)
        {
            var response = new PatientValueResponse();
            var user = _userRepository.Get(p => p.Id == request.PatientId);

            if (user == null)
            {
                response.Code = "400";
                response.Errors.Add("Kullanıcı bulunamadı");
                return response;
            }

            var values = _valueRepository.GetList(p => p.UserId == request.PatientId);

            //TODO kendi hastası dışında hastaları görüntüleyemesin

           var isDoctorPatient= _patientRepository.Get(p => p.DoctorId == request.UserId && p.UserId == request.PatientId);

           if (isDoctorPatient == null)
           {
               response.Code = "400";
               response.Errors.Add("Sizin olmayan bi hastanın verilerine erişmeye çalıştınız.");
               return response;
            }

            if (values == null)
            {
                response.Code = "400";
                response.Errors.Add("Hastaya ait veri bulunamadı");
                
                //Todo patlarsak burdan patlarız

            }

            response.Name = user.Name;
            response.Surname = user.Surname;
            response.Oxygen = values.Where(p => p.DataType == DataValueType.OXYGEN).ToList();
            response.Pulses = values.Where(p => p.DataType == DataValueType.PULSE).ToList();
            response.Temperatures = values.Where(p => p.DataType == DataValueType.TEMPERATURE).ToList();
            response.Code = "200";
            response.Message = "Hastaya ait veriler getirildi.";

            return response;
        }
        public GetPatientDateRangeResponse GetPatientHistoryDateRange(GetPatientDateRangeRequest request)
        {
            var response = new GetPatientDateRangeResponse();

            if (request.NewDate.Day - request.OldDate.Day >= 7)
            {
                response.Code = "400";
                response.Errors.Add("Tarih aralığı 7 günden fazla olamaz");
                return response;
            }

            var patient = _userRepository.Get(p => p.Id == request.PatientId);

            if (patient == null)
            {
                response.Code = "400";
                response.Errors.Add("Kullanıcı bulunamadı");
                return response;
            }
            var isDoctorPatient = _patientRepository.Get(p => p.DoctorId == request.UserId && p.UserId == request.PatientId);

            if (isDoctorPatient == null)
            {
                response.Code = "400";
                response.Errors.Add("Sizin olmayan bi hastanın verilerine erişmeye çalıştınız.");
                return response;
            } 

            var values = _valueRepository.GetList(p => p.UserId == request.PatientId && p.SaveDate <= request.NewDate && p.SaveDate >= request.OldDate);

            if (values == null)
            {
                response.Code = "200";
                response.Message = "Hastaya ait veri bulunamadı";
            }

            response.Name = patient.Name;
            response.Surname = patient.Surname;
            response.Oxygen = values.Where(p => p.DataType == DataValueType.OXYGEN).ToList();
            response.Pulses = values.Where(p => p.DataType == DataValueType.PULSE).ToList();
            response.Temperatures = values.Where(p => p.DataType == DataValueType.TEMPERATURE).ToList();
            response.Message = "Hastaya ait veriler getirildi.";
            response.Code = "200";
            return response;
        }
        public DoctorGetRelationsResponse GetConnections(DoctorGetRelationsRequest request)
        {

            var response = new DoctorGetRelationsResponse();

            var connectionList= _connectionRepository.GetList(p => p.ParentUserId == request.UserId && p.RequestStatus == true && p.Status == false);

            for (int i = 0; i < connectionList.Count; i++)
            {
                var connection = connectionList[i];
                var connectionRequest = new ConnectionRequestModel();
                connectionRequest.RequestStatus = connection.RequestStatus;
                connectionRequest.UserID = connection.ChildUserId;
                var user= _userRepository.Get(p => p.Id == connection.ChildUserId);
                connectionRequest.id = connection.Id;
                connectionRequest.Name = user.Name;
                connectionRequest.Surname = user.Surname;
                response.ConnectionRequests.Add(connectionRequest);



            }

            if (connectionList.Count == 0)
            {
                response.Message = "İstek Bulunamadı";
                response.Code = "200";
                return response;
            }


            response.Message = "İstekler getirildi";
            response.Code = "200";
            return response;

        }

        public ConnectionAcceptRefuseResponse ConnectionAcceptRefuse(ConnectionAcceptRefuseRequest request)
        {
            var response = new ConnectionAcceptRefuseResponse();

            var connection =_connectionRepository.Get(p => p.Id == request.Id);
            if (connection == null)
            {
                response.Code = "400";
                response.Errors.Add("istek bulunamadı");
                return response;
            }

            if (!request.isAccept)
            {
                connection.Status = false;
                connection.RequestStatus = false;
                connection.ModifiedDate=DateTime.Now;
                response.Message = "İstek reddedildi";

            }

            if (request.isAccept)
            {
                connection.Status = false;
                connection.RequestStatus = false;
                connection.ModifiedDate = DateTime.Now;
                response.Message = "İstek kabul edildi";

              
                var user = _patientRepository.Get(p => p.UserId == connection.ChildUserId);
                user.DoctorId = request.UserId;
                _patientRepository.Update(user);

            }

            _connectionRepository.Update(connection);

            response.Code = "200";
           // response.Message = "İstek kabul edildi";

            return response;
        }

        public DoctorGetLocationsResponse GetDoctorLocations(DoctorGetLocationsRequest request)
        {
            DoctorGetLocationsResponse response = new DoctorGetLocationsResponse();

            var patients = _patientRepository.GetList(p => p.DoctorId == request.UserId).Select(s=>s.UserId);
            if (patients.Count() == 0 )
            {
                response.Code = "400";
                response.Errors.Add("Hasta bulunamadı");
                return response;
            }
           
            foreach (var id in patients)
            { 
                var locationModel = new PatientLocationModel();
                
                var patient = _userRepository.Get(p => p.Id==id);
                locationModel.Name = patient.Name;
                locationModel.Surname = patient.Surname;

                var location = _locationRepository.Get(p => p.UserId == id);
                if (location == null)
                {
                    locationModel.lat = 0;
                    locationModel.lng = 0;
                    locationModel.Time = DateTime.Now;
                    locationModel.UserId = id;
                    response.Patients.Add(locationModel);
                }
                else
                {
                    locationModel.lat = location.LatitudeNow;
                    locationModel.lng = location.LongitudeNow;
                    locationModel.Time = location.ModifiedDate;
                    locationModel.UserId = id;
                    response.Patients.Add(locationModel);
                }
            }
            
           
            response.Code = "200";
            response.Message = "Hasta Konumları getirildi";

            return response;
        }


        public DoctorGetWarningsResponse DoctorGetWarnings(DoctorGetWarningsRequest request)
        {
            DoctorGetWarningsResponse response = new DoctorGetWarningsResponse();

            var patientsId = _patientRepository.GetList(p => p.DoctorId == request.UserId).Select(s => s.UserId);
            foreach (var id in patientsId)
            {
                var patient = new PatientInfoModel();
                
                var user = _userRepository.Get(p => p.Id == id);
                patient.UserId = user.Id;
                patient.Name = user.Name;
                patient.Surname = user.Surname;
                
                ValueTypeListModel UserWarnings =new ValueTypeListModel();
                var locationsWarnings =
                    _locationWarningRepository.GetList(p => p.UserId == id).OrderByDescending(o => o.Id);

               

                var warnings = _warningRepository.GetList(p => p.UserId == id).OrderByDescending(o => o.Id);
                var oxygenWarnings = warnings.Where(s => s.WarningData == WarningType.OXYGEN).OrderByDescending(o => o.Id);
                var pulseWarnings = warnings.Where(s => s.WarningData == WarningType.PULSE).OrderByDescending(o => o.Id);
                var temperatureWarnings = warnings.Where(s => s.WarningData == WarningType.TEMPERATURE).OrderByDescending(o => o.Id);
                var connectionWarnings = warnings.Where(s => s.WarningData == WarningType.CONNECTION).OrderByDescending(o => o.Id);
                foreach (var warning in oxygenWarnings)
                {
                    var valueModel = new ValueWarningModel();
                    valueModel.ValueType = warning.WarningData;
                    valueModel.ValueData = warning.WarningValue;
                    valueModel.time = warning.CreatedDate;
                    UserWarnings.Oxygen.Add(valueModel);
                }
                foreach (var warning in connectionWarnings)
                {
                    var valueModel = new ValueWarningModel();
                    valueModel.ValueType = warning.WarningData;
                    valueModel.ValueData = warning.WarningValue;
                    valueModel.time = warning.CreatedDate;
                    UserWarnings.Connections.Add(valueModel);
                }
                foreach (var warning in pulseWarnings)
                {
                    var valueModel = new ValueWarningModel();
                    valueModel.ValueType = warning.WarningData;
                    valueModel.ValueData = warning.WarningValue;
                    valueModel.time = warning.CreatedDate;
                    UserWarnings.Pulses.Add(valueModel);
                }
                foreach (var warning in temperatureWarnings)
                {
                    var valueModel = new ValueWarningModel();
                    valueModel.ValueType = warning.WarningData;
                    valueModel.ValueData = warning.WarningValue;
                    valueModel.time = warning.CreatedDate;
                    UserWarnings.Temperatures.Add(valueModel);
                }
                foreach (var warning in locationsWarnings)
                {
                    var valueModel = new ValueWarningModel();
                    valueModel.ValueType =WarningType.LOCATION;
                    valueModel.ValueData = warning.Range;
                    valueModel.time = warning.CreatedDate;
                    UserWarnings.Locations.Add(valueModel);
                }

                patient.ValueTypeListModels.Add(UserWarnings);
                response.Patients.Add(patient);
            }

            response.Message = "Hastalara ait uyarılar getirildi";
            response.Code = "200";
            return response;
        }

        public PatientDeleteResponse DeletePatient(PatientDeleteRequest request)
        {
            PatientDeleteResponse response = new PatientDeleteResponse();

            var patient = _patientRepository.Get(p => p.UserId == request.PatientId);

            if (patient.DoctorId != request.UserId)
            {
                response.Code = "400";
                response.Errors.Add("Size ait olmayan bir hastayı silmeye çalışıyorsunuz");
                return response;
            }

            patient.DoctorId = 0;
            _patientRepository.Update(patient);

            response.Message = "Hasta sizin hastalarınızdan çıkarıldı";
            response.Code = "200";
            return response;
        }

        public WarningListResponse GetWarningList(WarningListRequest request)
        {
            var response = new WarningListResponse();

            var drid = request.UserId;

            var AllWarnings = _warningRepository.GetList(p => p.SaveDate > DateTime.Now.AddDays(-30));
            //Son bir aylık uyarılar getirildi.
            var LocationWarnings = _locationWarningRepository.GetList(p => p.CreatedDate > DateTime.Now.AddDays(-30));
            //Konum uyarıları 
            foreach(var lw in LocationWarnings)
            {
                var LocationWarning = new Warning();

                LocationWarning.WarningData = WarningType.LOCATION;
                LocationWarning.WarningValue = lw.Range;
                LocationWarning.CreatedDate = lw.CreatedDate;
                LocationWarning.UserId = lw.UserId;
                AllWarnings.Add(LocationWarning);
            }

           // LocationWarnings.OrderByDescending(o=>o.CreatedDate);

            var PatientIdList = _patientRepository.GetList(p => p.DoctorId == drid).Select(s=>s.UserId);
            //Hasta id listesi
            //AllWarnings.OrderBy(o => o.CreatedDate);
          //  AllWarnings.OrderByDescending(o => o.CreatedDate);
            foreach (var warning in AllWarnings)
            {
                if (PatientIdList.Contains(warning.UserId))//43,1,68
                {
                    var PatientUser = _userRepository.Get(p => p.Id == warning.UserId);
                    var model = new WarningInfoModel();
                    model.Name = PatientUser.Name+" "+PatientUser.Surname;
                    model.time = warning.CreatedDate;
                    model.ValueData = warning.WarningValue;
                    model.ValueType = warning.WarningData;

                    response.Warnings.Add(model);
                }

            }

            response.Warnings = response.Warnings.OrderByDescending(o => o.time).ToList();

            //response.Warnings.OrderByDescending(o => o.time);
            response.Message = "Uyarılar getirildi.";
            response.Code = "200";
            return response;
        }
    }
}

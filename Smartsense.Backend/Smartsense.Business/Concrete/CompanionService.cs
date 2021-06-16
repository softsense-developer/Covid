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

    public class CompanionService :ICompanionService
    {
        private readonly ICompanionRepository _companionRepository;
        private readonly IDoctorRepository _doctorRepository;
        private readonly IValueRepository _valueRepository;
        private readonly IPatientRepository _patientRepository;
        private readonly IUserRepository _userRepository;
        private readonly IConnectionRepository _connectionRepository;
        private readonly ILocationRepository _locationRepository;
        private readonly IWarningRepository _warningRepository;
        private readonly ILocationWarningRepository _locationWarningRepository;

        public CompanionService(ICompanionRepository companionRepository, IDoctorRepository doctorRepository, IValueRepository valueRepository, ILocationWarningRepository locationWarningRepository, IWarningRepository warningRepository, IPatientRepository patientRepository, IUserRepository userRepository, ILocationRepository locationRepository, IConnectionRepository connectionRepository)
        {
            _doctorRepository = doctorRepository;
            _valueRepository = valueRepository;
            _patientRepository = patientRepository;
            _userRepository = userRepository;
            _connectionRepository = connectionRepository;
            _locationRepository = locationRepository;
            _warningRepository = warningRepository;
            _locationWarningRepository = locationWarningRepository;
            _companionRepository = companionRepository;
        }


        public CompanionGetPatientsResponse GetInfo(CompanionGetPatientsRequest request)
        {
            var response = new CompanionGetPatientsResponse();
            // var x= new DoctorGetPatientsResponse.PatientsData();
            var userid = request.UserId;

            var patients = _companionRepository.GetList(p => p.CompanionUserId == userid);

            if (patients == null)
            {
                response.Code = "400";
                response.Errors.Add("Hastalar bulunamadı.");
                return response;
            }

            var PatientsId = patients.Select(s => s.PatientUserId).ToList();



            for (int i = 0; i < PatientsId.Count; i++)
            {
                var x = new CompanionGetPatientsResponse.PatientData();
                var id = PatientsId[i];
                var patient = _patientRepository.Get(p => p.UserId == id);
                var PatientStatus = patient.UserStatus;
                var patientUser = _userRepository.Get(p => p.Id == id);
                x.Name = patientUser.Name;
                x.Surname = patientUser.Surname;
                x.Diagnosis = patient.Diagnosis;
                x.PatientStatus = patient.UserStatus;
                var patientValues = _valueRepository.GetList(p => p.UserId == id);


                var oksijen = patientValues.Where(w => w.DataType == DataValueType.OXYGEN).Select(s => s.DataValue).LastOrDefault();
                var nabız = patientValues.Where(w => w.DataType == DataValueType.PULSE).Select(s => s.DataValue).LastOrDefault();
                var sıcaklık = patientValues.Where(w => w.DataType == DataValueType.TEMPERATURE).Select(s => s.DataValue).LastOrDefault();

                x.OxygenWarning = x.PulseWarning = x.TemperatureWarning = ValueStatus.NORM;

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

        public CompanionGetLocationsResponse GetCompanionLocations(CompanionGetLocationsRequest request)
        {
            CompanionGetLocationsResponse response = new CompanionGetLocationsResponse();

            var patients = _companionRepository.GetList(p => p.CompanionUserId == request.UserId).Select(s => s.PatientUserId);
            if (patients.Count() == 0)
            {
                response.Code = "400";
                response.Errors.Add("Hasta bulunamadı");
                return response;
            }

            foreach (var id in patients)
            {
                var locationModel = new PatientLocationModel();

                var patient = _userRepository.Get(p => p.Id == id);
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

        public PatientValueResponse GetPatientData(PatientValueRequest request)
        {
            var response = new PatientValueResponse();
            var user = _userRepository.Get(p => p.Id == request.PatientId);

            if (user == null)
            {
                response.Code = "400";
                response.Errors.Add("Kullanıcı bulunamadı");
            }

            var isDoctorPatient = _companionRepository.Get(p => p.CompanionUserId == request.UserId && p.PatientUserId == request.PatientId);

            if (isDoctorPatient == null)
            {
                response.Code = "400";
                response.Errors.Add("Sizin olmayan bi hastanın verilerine erişmeye çalıştınız.");
                return response;
            }

            var companion =_companionRepository.Get(p => p.CompanionUserId == request.UserId && p.PatientUserId==request.PatientId);

            if (companion == null)
            {
                response.Code = "400";
                response.Errors.Add("Hastaya ait veri bulunamadı");
                return response;
            }
            var values = _valueRepository.GetList(p => p.UserId == request.PatientId);

            if (values == null)
            {
                response.Code = "400";
                response.Errors.Add("Hastaya ait veri bulunamadı");
                return response;
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

        public CompanionGetWarningsResponse CompanionGetWarnings(CompanionGetWarningsRequest request)
        {
            CompanionGetWarningsResponse response = new CompanionGetWarningsResponse();

            var patientsId = _companionRepository.GetList(p => p.CompanionUserId == request.UserId).Select(s => s.PatientUserId);
            foreach (var id in patientsId)
            {
                var patient = new PatientInfoModel();

                var user = _userRepository.Get(p => p.Id == id);
                patient.UserId = user.Id;
                patient.Name = user.Name;
                patient.Surname = user.Surname;

                ValueTypeListModel UserWarnings = new ValueTypeListModel();
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
                    valueModel.ValueType = WarningType.LOCATION;
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

        public CompanionWarningsResponse WarningList(CompanionWarningsRequest request)
        {
            var response = new CompanionWarningsResponse();

            var patientsId = _companionRepository.GetList(p => p.CompanionUserId == request.UserId).Select(s => s.PatientUserId);
            //sıralamayı düzelt
            List<Warning> warnings = new List<Warning>();

            foreach (var patid in patientsId)
            {
                var warningstemp =
                    _warningRepository.GetList(p => p.UserId == patid && p.SaveDate > DateTime.Now.AddDays(-30));
                 foreach (var VARIABLE in warningstemp)
                 {
                     warnings.Add(VARIABLE);
                 }

                var locationWarnings = _locationWarningRepository.GetList(p =>
                    p.UserId == patid && p.CreatedDate > DateTime.Now.AddDays(-30));
                foreach (var w in locationWarnings)
                {
                    var warning = new Warning();
                    warning.SaveDate = w.CreatedDate;
                    warning.UserId = w.UserId;
                    warning.WarningData = WarningType.LOCATION;
                    warning.WarningValue = w.Range;
                    warnings.Add(warning);

                }

               
                
            }
                // warnings.OrderByDescending(o => o.CreatedDate);
            foreach (var warning in warnings)
            {
                var model = new WarningInfoModel();
                var patient = _userRepository.Get(p => p.Id == warning.UserId);
                model.Name = patient.Name + " " + patient.Surname;
                model.ValueType = warning.WarningData;
                model.ValueData = warning.WarningValue;
                model.time = warning.SaveDate;
                response.Warnings.Add(model);
            }

            //Todo zamana göre sıralama düzgün çalışmıyor
            response.Warnings.OrderByDescending(o => o.time);
            response.Message = "Hastalara ait uyarılar getirildi";
            response.Code = "200";
            return response;
        }
    }
    
}

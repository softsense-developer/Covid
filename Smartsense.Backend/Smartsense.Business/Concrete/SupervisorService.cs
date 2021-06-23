using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Smartsense.Business.Abstract;
using Smartsense.Data.Abstract;
using Smartsense.Entity.DTO;
using Smartsense.Entity.Enums;
using Smartsense.Entity.Models;
using Smartsense.Entity.Validators;

namespace Smartsense.Business.Concrete
{
    public class SupervisorService : ISupervisorService
    {
        private readonly IDoctorRepository _doctorRepository;
        private readonly IValueRepository _valueRepository;
        private readonly IPatientRepository _patientRepository;
        private readonly IUserRepository _userRepository;
        private readonly ISupervisorRepository _supervisorRepository;
        private readonly IConnectionRepository _connectionRepository;
        private readonly ILocationRepository _locationRepository;
        private readonly IHospitalRepository _hospitalRepository;
        private readonly ILocationWarningRepository _locationWarningRepository;
        private readonly IWarningRepository _warningRepository;

        public SupervisorService(IDoctorRepository doctorRepository, IWarningRepository warningRepository, ILocationWarningRepository locationWarningRepository, IHospitalRepository hospitalRepository,IValueRepository valueRepository, ILocationRepository locationRepository, IPatientRepository patientRepository,IUserRepository userRepository, ISupervisorRepository supervisorRepository, IConnectionRepository connectionRepository)
        {
            _doctorRepository = doctorRepository;
            _valueRepository = valueRepository;
            _patientRepository = patientRepository;
            _userRepository = userRepository;
            _supervisorRepository = supervisorRepository;
            _connectionRepository = connectionRepository;
            _locationRepository = locationRepository;
            _hospitalRepository = hospitalRepository;
            _locationWarningRepository = locationWarningRepository;
            _warningRepository = warningRepository;
        }

        public GetDoctorsResponse GetDoctorsInfo(SupervisorGetDoctorsRequest request)
        {
            
            var response = new GetDoctorsResponse();
            var Supervisor = _userRepository.Get(p => p.Id == request.UserId).RoleId;
            if (Supervisor != 3)
            {
                response.Code = "400";
                response.Errors.Add("Yetkiniz bulunmamaktadır");
                return response;
            }

            var doctorsid = _doctorRepository.GetList(p => p.SupervisorId==request.UserId);

            for (int k = 0; k < doctorsid.Count;k++)
            {
                var doctor = doctorsid[k];
                var doktorlarInfoModel = new DoktorlarInfoModel();
                var doctorInfo = _userRepository.Get(p => p.Id == doctor.UserId);
                doktorlarInfoModel.DoktorAdi = doctorInfo.Name;
                doktorlarInfoModel.DoktorID = doctorInfo.Id;

                var patients = _patientRepository.GetList(p => p.DoctorId == doctorInfo.Id);
                var patientsid = patients.Select(s => s.UserId).ToList();

                for (int i = 0; i < patients.Count(); i++)
                {
                    var id = patientsid[i];

                   var user= _userRepository.Get(p => p.Id == id);
                   var patientValues = _valueRepository.GetList(p => p.UserId == id);


                   var oksijen = patientValues.Where(w => w.DataType == DataValueType.OXYGEN).Select(s => s.DataValue).LastOrDefault();
                   var nabız = patientValues.Where(w => w.DataType == DataValueType.PULSE).Select(s => s.DataValue).LastOrDefault();
                   var sıcaklık = patientValues.Where(w => w.DataType == DataValueType.TEMPERATURE).Select(s => s.DataValue).LastOrDefault();

                   var warning = _locationWarningRepository.GetList(p=>p.UserId==user.Id&& p.CreatedDate>DateTime.Now.AddMinutes(-30)).LastOrDefault();
                   
                    
                   var model = new HastalarInfoModel();
                   model.LocationWarning = model.OxygenWarning =
                       model.PulseWarning = model.TemperatureWarning = ValueStatus.NORM;
                   model.HastaId = user.Id;
                   model.name = user.Name;
                   model.surname = user.Surname;
                   model.oxygen = oksijen;
                   model.pulse = nabız;
                   model.temperature = sıcaklık;
                   model.LocationWarning = ValueStatus.NORM;
                   if (warning != null)
                   {
                       model.LocationWarning = ValueStatus.HIGH;
                    }

                  var PatientStatus= _patientRepository.Get(p => p.UserId == user.Id).UserStatus;
                   if (PatientStatus == UserStatus.QUARANTINE_AT_HOME)
                   {

                       if (oksijen <= 92)
                       {
                           model.OxygenWarning = ValueStatus.LOW;
                       }

                       if (sıcaklık >= 38)
                       {
                           model.TemperatureWarning = ValueStatus.HIGH;
                       }
                       
                       if (nabız >= 110)
                       {
                           model.PulseWarning = ValueStatus.HIGH;
                       }
                       if (nabız <= 50)
                       {
                           model.PulseWarning = ValueStatus.LOW;
                       }

                   }
                   if (PatientStatus == UserStatus.QUARANTINE_IN_HOSPITAL)
                   {

                       if (oksijen <= 90)
                       {
                           model.OxygenWarning = ValueStatus.LOW;
                       }

                       if (sıcaklık >= 38.3)
                       {
                           model.TemperatureWarning = ValueStatus.HIGH;
                       }

                       if (nabız >= 110)
                       {
                           model.PulseWarning = ValueStatus.HIGH;
                       }
                       if (nabız <= 50)
                       {
                           model.PulseWarning = ValueStatus.LOW;
                       }

                   }

                   model.diagnosis= _patientRepository.Get(p => p.UserId == user.Id).Diagnosis;

                    doktorlarInfoModel.HastalarInfoModels.Add(model);

                }
                response.DoktorInfoModels.Add(doktorlarInfoModel );

                    


            }

            response.Message = "Doktorlar getirildi.";
            response.Code = "200";
            return response;

        }

        public SupervisorPromotionResponse DoctorPromotion(SupervisorPromotionRequest request)
        {
            var response = new SupervisorPromotionResponse();
            var Supervisor = _userRepository.Get(p => p.Id == request.UserId);
            
            if (Supervisor.RoleId != 3)
            {
                response.Code = "400";
                response.Errors.Add("Yetkiniz bulunmamaktadır");
                return response;
            }
            var user =_userRepository.Get(p => p.Id == request.DoctorId );

            if (user == null)
            {
                response.Code = "400";
                response.Errors.Add("Kullanıcı bulunamadı");
                return response;
            }
            if (user.RoleId == 2)
            {
                response.Code = "400";
                response.Errors.Add("Kullanıcı zaten doktor");
                return response;
            }
            var dr = _doctorRepository.Get(p => p.UserId == request.DoctorId);
           if (dr != null)
           {
               response.Code = "400";
               response.Errors.Add("Kullanıcı zaten bir doktor.");
               return response;

            }
            // doktor terfi ettirilme isteği gönderilecek
            //user.RoleId = 2;

            //_userRepository.Update(user);

            //var hospitalId = _supervisorRepository.Get(p => p.UserId == request.UserId);
            //var doctor = new Doctor();
            //doctor.UserId = request.DoctorId;
            //doctor.SupervisorId = request.UserId;
            //doctor.CreatedDate=DateTime.Now;
            //doctor.HospitalId = hospitalId.HospitalId;
            //_doctorRepository.Add(doctor);

            //response.Code = "200";
            //response.Message = "" + user.Id + " li kullanıcı doktor rolüne terfi ettirildi";

            //return response;
            var connection = new Connection();

            connection.ParentUserId = request.UserId;
            connection.ChildUserId = request.DoctorId;
            connection.RequestStatus = false;
            connection.Status = true;
            connection.CreatedDate=DateTime.Now;
            _connectionRepository.Add(connection);

            response.Code = "200";
            response.Message = "" + user.Id + " li kullanıcıya doktor rolüne terfi isteği gönderildi";

            return response;


        }
        public SupervisorDemotionResponse DoctorDemotion(SupervisorDemotionRequest request)
        {
            var response = new SupervisorDemotionResponse();
            var Supervisor = _userRepository.Get(p => p.Id == request.UserId).RoleId;
            if (Supervisor != 3)
            {
                response.Code = "400";
                response.Errors.Add("Yetkiniz bulunmamaktadır");
                return response;
            }
            var user = _userRepository.Get(p => p.Id == request.DoctorId);

            if (user == null)
            {
                response.Code = "400";
                response.Errors.Add("Kullanıcı bulunamadı");
                return response;
            }
            if (user.RoleId == 1)
            {
                response.Code = "400";
                response.Errors.Add("Kullanıcı zaten doktor değil" );
                return response;
            }
            user.RoleId = 1;

            _userRepository.Update(user);

            var doctor= _doctorRepository.Get(p => p.UserId == request.DoctorId);

            var patients = _patientRepository.GetList(p => p.DoctorId == request.DoctorId);

            foreach (var VARIABLE in patients)
            {
                VARIABLE.DoctorId = 0;
                _patientRepository.Update(VARIABLE);
            }

            _doctorRepository.Delete(doctor);

            
            //TODO doctor tablosundan düşürülmesi gerek

            response.Code = "200";
            response.Message = "" + user.Id + " id li kullanıcı doktor rolünden düşürüldü";

            return response;


        }

        public SupervisorGetLocationsResponse GetSupervisorLocations(SupervisorGetLocationsRequest request)
        {
            var response = new SupervisorGetLocationsResponse();
            var Supervisor = _userRepository.Get(p => p.Id == request.UserId).RoleId;
            if (Supervisor != 3)
            {
                response.Code = "400";
                response.Errors.Add("Yetkiniz bulunmamaktadır");
                return response;
            }
            var doctorsid = _doctorRepository.GetList(p => p.SupervisorId == request.UserId).Select(s => s.UserId);

            foreach (var id in doctorsid)
            {
               var patients= _patientRepository.GetList(p => p.DoctorId == id);
               foreach (var patient in patients)
               {
                   var locationModel = new PatientLocationModel();
               var location= _locationRepository.Get(p => p.UserId == patient.UserId);
               var user = _userRepository.Get(p => p.Id == patient.UserId);
               if (location == null)
               {
                   locationModel.Name = user.Name;
                   locationModel.Surname = user.Surname;
                   locationModel.lat = 0;
                   locationModel.lng = 0;
                   locationModel.Time = default(DateTime);
                   locationModel.UserId = id;
                   response.SupervisorPatients.Add(locationModel);
                   break;
               }   
               
               locationModel.Name = user.Name;
               locationModel.Surname = user.Surname;
               locationModel.lat = location.LatitudeNow;
               locationModel.lng = location.LongitudeNow;
               locationModel.Time = location.ModifiedDate;
               locationModel.UserId = id;
               response.SupervisorPatients.Add(locationModel); 
               }
              

            }

            response.Code = "200";
            response.Message = " Hastalar getirildi";

            return response;
        }

        public PutHospitalResponse PutHospital(PutHospitalRequest request)
        {
            var response = new PutHospitalResponse();
            var validator = new PutHospitalValidator();
            

            var validatorResult = validator.Validate(request);

            if (!validatorResult.IsValid)
            {
                foreach (var err in validatorResult.Errors)
                {
                    response.Errors.Add(err.ErrorMessage);
                }
                response.Code = "400";
                response.Errors.Add("Doğrulama Hatası");
                return response;
            }

            var supervisor =_supervisorRepository.Get(p => p.UserId == request.UserId);

            if (supervisor == null)
            {
                response.Code = "400";
                response.Errors.Add("Yetkisiz erişim ");
                return response;
            }

            var hospital = _hospitalRepository.Get(p => p.Id == supervisor.HospitalId);
            if (hospital == null)
            {
                response.Code = "400";
                response.Errors.Add("Hastane bulunamadı.");
                return response;
            }

            hospital.HospitalAddress = request.Address;
            hospital.HospitalName = request.HospitalName;
            hospital.HospitalCapacity = request.HospitalCapacity;

            _hospitalRepository.Update(hospital);

            response.Code = "200";
            response.Message = " Hastane bilgisi düzenlendi";
            return response;
        }

        public GetSupervisorHospitalResponse GetHospital(GetSupervisorHospitalRequest request)
        {
            var response = new GetSupervisorHospitalResponse();

            var supervisor = _supervisorRepository.Get(p => p.UserId == request.UserId);

            if (supervisor == null)
            {
                response.Code = "400";
                response.Errors.Add("Yetkisiz erişim ");
                return response;
            }

            var hospital = _hospitalRepository.Get(p => p.Id == supervisor.HospitalId);
            if (hospital == null)
            {
                response.Code = "400";
                response.Errors.Add("Hastane bulunamadı.");
                return response;
            }

            response.Address = hospital.HospitalAddress;
            response.HospitalName = hospital.HospitalName;
            response.HospitalCapacity = hospital.HospitalCapacity;

            response.Code = "200";
            response.Message = " Hastane bilgisi getirildi";
            return response;
        }

        public GetHospitalDoctorsResponse GetHospitalDoctors(GetHospitalDoctorsRequest request)
        {
            GetHospitalDoctorsResponse response = new GetHospitalDoctorsResponse();

            var supervisor = _supervisorRepository.Get(p => p.UserId == request.UserId);

            if (supervisor == null)
            {
                response.Code = "400";
                response.Errors.Add("Yetkisiz erişim ");
                return response;
            }

            var doctors = _doctorRepository.GetList(p => p.SupervisorId == request.UserId);

            foreach (var doctor in doctors)
            {
               var user = _userRepository.Get(p => p.Id == doctor.UserId);

               DoctorModel model = new DoctorModel();

               model.DoctorId = user.Id;
               model.DoctorName = user.Name;
               model.DoctorSurname = user.Surname;
               response.Doctors.Add(model);
            }

            response.Code = "200";
            response.Message = " Doktorlar  getirildi";
            return response;
        }

        public GetPatientDataResponse GetPatientData(GetPatientDataRequest request)
        {
            var response = new GetPatientDataResponse();

            var supervisor = _userRepository.Get(p => p.Id == request.UserId);
           

            if (supervisor.RoleId != 3)
            {
                response.Code = "400";
                response.Errors.Add("Yetkisiz erişim ");
                return response;
            }
            var user = _userRepository.Get(p => p.Id==request.PatientId);
            if (user == null)
            {
                response.Code = "400";
                response.Errors.Add("Kullanıcı bulunamadı");
            }

            var values = _valueRepository.GetList(p => p.UserId == request.PatientId);

            if (values == null)
            {
                response.Code = "400";
                response.Errors.Add("Hastaya ait veri bulunamadı");
                //return response;
                //Todo patlarsak burdan patlarız

            }

            response.Name = user.Name;
            response.Surname = user.Surname;
            response.Oxygen = values.Where(p => p.DataType == DataValueType.OXYGEN).ToList();
            response.Pulses = values.Where(p => p.DataType == DataValueType.PULSE).ToList();
            response.Temperatures = values.Where(p => p.DataType == DataValueType.TEMPERATURE).ToList();


            response.Code = "200";
            response.Message = " Doktorlar  getirildi";
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
            var doctor = _patientRepository.Get( p=>p.UserId == request.PatientId);
            var isDoctorPatient =
                _doctorRepository.Get(p => p.SupervisorId == request.UserId && p.UserId == doctor.DoctorId);

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
    }
    }


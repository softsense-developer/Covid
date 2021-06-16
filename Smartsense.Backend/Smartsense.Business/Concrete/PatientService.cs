using System;
using System.Linq;
using System.Net.Mail;
using Smartsense.Business.Abstract;
using Smartsense.Data.Abstract;
using Smartsense.Entity.DTO;
using Smartsense.Entity.Enums;
using Smartsense.Entity.Models;
using Smartsense.Entity.Validators;

namespace Smartsense.Business.Concrete
{
    public class PatientService : IPatientService
    {
        private readonly IUserRepository _userRepository;
        private readonly IPatientRepository _patientRepository;
        private readonly IConnectionRepository _connectionRepository;
        private readonly ILocationRepository _locationRepository;
        private readonly ILocationWarningRepository _locationWarningRepository;
        private readonly IWarningRepository _warningRepository;
        private readonly IDoctorRepository _doctorRepository;
        private readonly IHospitalRepository _hospitalRepository;
        private readonly ICompanionRepository _companionRepository;
        private readonly ISupervisorRepository _supervisorRepository;
        private readonly IMailVerifyRepository _mailVerifyRepository;
        private readonly IValueRepository _valueRepository;

        public PatientService(IUserRepository userRepository, ICompanionRepository companionRepository,
            IHospitalRepository hospitalRepository, IDoctorRepository doctorRepository,
            IPatientRepository patientRepository, IWarningRepository warningRepository,
            IConnectionRepository connectionRepository, ILocationRepository locationRepository,
            ILocationWarningRepository locationWarningRepository,ISupervisorRepository supervisorRepository, IMailVerifyRepository mailVerifyRepository,IValueRepository valueRepository)
        {
            _userRepository = userRepository;
            _patientRepository = patientRepository;
            _connectionRepository = connectionRepository;
            _locationRepository = locationRepository;
            _locationWarningRepository = locationWarningRepository;
            _warningRepository = warningRepository;
            _doctorRepository = doctorRepository;
            _hospitalRepository = hospitalRepository;
            _companionRepository = companionRepository;
            _supervisorRepository = supervisorRepository;
            _mailVerifyRepository = mailVerifyRepository;
            _valueRepository = valueRepository;
            
        }

        public PatientRelationDoctorResponse AddDoctor(PatientRelationDoctorRequest request)
        {
            var response = new PatientRelationDoctorResponse();
            var connection = new Connection();
            connection.ChildUserId = request.UserId;
            connection.ParentUserId = request.DoctorId;
            connection.RequestStatus = true;
            connection.Status = false;
            connection.CreatedDate = DateTime.Now;

            var patient = _patientRepository.Get(p => p.UserId == request.UserId);
            if (patient == null)
            {
                response.Code = "400";
                response.Errors.Add("Önce hastaya ait verileri doldurunuz");
                return response;
            }

            var oldRequest = _connectionRepository.Get(p =>
                p.RequestStatus == true && p.Status == false && p.ChildUserId == request.UserId &&
                p.ParentUserId == request.DoctorId && p.CreatedDate > DateTime.Now.AddDays(-1));
            if (oldRequest != null)
            {
                response.Code = "400";
                response.Errors.Add("son 24 saatte ay doktora sadece 1 istek atabilirsiniz");
               return response;
            }

            var doctor = _doctorRepository.Get(p => p.UserId == request.DoctorId);
            if (doctor == null)
            {
                response.Code = "400";
                response.Errors.Add(request.DoctorId + " id li doktor bulunamadı");
                return response;
            }

            // patient.DoctorId = request.DoctorId;
            // _patientRepository.Update(patient);
            _connectionRepository.Add(connection);


            response.Code = "200";
            response.Message = "Doktor eklendi.";
            return response;
        }

        public LocationPostResponse AddQuarantineLocation(LocationPostRequest request)
        {
            var response = new LocationPostResponse();

            var quarantine = _locationRepository.Get(p => p.UserId == request.UserId);
            var location = new Location();
            if (quarantine != null && quarantine.QuarantineEnd < DateTime.Now)
            {
                location.LatitudeQuarantine = request.LatitudeQuarantine;
                location.LongitudeQuarantine = request.LongitudeQuarantine;
                location.CreatedDate = DateTime.Now;
                location.QuarantineStart = DateTime.Now;
                location.QuarantineEnd = DateTime.Now.AddDays(10);

                _locationRepository.Update(quarantine);
                response.Message = "Karantina Konumu Güncellendi";
                response.Code = "200";
                return response;
            }

            if (quarantine != null && quarantine.QuarantineEnd > DateTime.Now)
            {
                response.Errors.Add("Karantina süresi dolmadan değişiklik yapılamaz");
                response.Code = "400";
                return response;
            }

            //TODO Karantina başlangıç tarihi kullanıcıdan mı gelsin sistem mi versin
            location.UserId = request.UserId;
            location.LatitudeQuarantine = request.LatitudeQuarantine;
            location.LongitudeQuarantine = request.LongitudeQuarantine;
            location.CreatedDate = DateTime.Now;
            location.LatitudeNow = request.LatitudeQuarantine;
            location.LongitudeNow = request.LongitudeQuarantine;
            location.QuarantineStart = DateTime.Now;
            location.QuarantineEnd = DateTime.Now.AddDays(10); //10 gün karantina süresi
            _locationRepository.Add(location);


            response.Message = "Karantina Konumu Eklendi";
            response.Code = "200";
            return response;
        }

        public LocationPutResponse PutNowLocation(LocationPutRequest request)
        {
            var response = new LocationPutResponse();

            var quarantine = _locationRepository.Get(p => p.UserId == request.UserId);

            quarantine.LatitudeNow = request.LatitudeNow;
            quarantine.LongitudeNow = request.LongitudeNow;
            var distance = CalculateDistance(quarantine);
            if (distance > 50)
            {
                var warning = new LocationWarning();
                warning.UserId = request.UserId;
                warning.Range = (decimal) distance;
                warning.LatitudeWarning = (decimal) request.LatitudeNow;
                warning.LongitudeWarning = (decimal) request.LongitudeNow;
                warning.CreatedDate = DateTime.Now;
                _locationWarningRepository.Add(warning);
            }

            quarantine.ModifiedDate = DateTime.Now;
            _locationRepository.Update(quarantine);

            response.Message = "Anlık Konum Eklendi";
            response.Code = "200";
            return response;
        }

        public PatientGetLocationResponse GetPatientLocation(PatientGetLocationRequest request)
        {
            var response = new PatientGetLocationResponse();

            var location = _locationRepository.Get(p => p.UserId == request.UserId);

            if (location == null)
            {
                response.Code = "400";
                response.Errors.Add("Konum bulunamadı");
                return response;
            }

            response.LatitudeNow = location.LatitudeNow;
            response.LongitudeNow = location.LongitudeNow;
            response.Time = location.ModifiedDate;

            response.Message = "Kulanıcı konumu getirildi";
            response.Code = "200";
            return response;
        }

        public double CalculateDistance(Location point1)
        {
            var d1 = point1.LatitudeQuarantine * (Math.PI / 180.0);
            var num1 = point1.LongitudeQuarantine * (Math.PI / 180.0);
            var d2 = point1.LatitudeNow * (Math.PI / 180.0);
            var num2 = point1.LongitudeNow * (Math.PI / 180.0) - num1;
            var d3 = Math.Pow(Math.Sin((d2 - d1) / 2.0), 2.0) +
                     Math.Cos(d1) * Math.Cos(d2) * Math.Pow(Math.Sin(num2 / 2.0), 2.0);
            return 6376500.0 * (2.0 * Math.Atan2(Math.Sqrt(d3), Math.Sqrt(1.0 - d3)));
        }

        public PatientAddInfoResponse AddInfo(PatientAddInfoRequest request)
        {
            var response = new PatientAddInfoResponse();

            var validator = new PatientAddInfoValidator();
            var validatorResult = validator.Validate(request);

            if (!validatorResult.IsValid)
            {
                foreach (var err in validatorResult.Errors) response.Errors.Add(err.ErrorMessage);
                response.Code = "400";
                response.Errors.Add("Doğrulama Hatası");
                return response;
            }


            var patient = new Patient
            {
                DateOfBirth = request.DateOfBirth,
                CreatedDate = DateTime.Now,
                Diagnosis = request.Diagnosis,
                Gender = request.Gender,
                UserId = request.UserId,
                IdentityNumber = request.IdentityNumber,
                BloodGroup = request.BloodGroup
            };


            var patientData = _patientRepository.Get(p => p.UserId == request.UserId);
            if (patientData != null)
            {
                response.Errors.Add("Hastaya ait veri bulunmaktadır.");
                response.Code = "400";
                return response;
            }

            _patientRepository.Add(patient);

            response.Message = "Hastaya ait veriler eklendi.";
            response.Code = "200";
            return response;
        }

        public PatientGetInfoResponse GetInfo(PatientGetInfoRequest request)
        {
            var response = new PatientGetInfoResponse();
            var userid = request.UserId;

            var user = _userRepository.Get(p => p.Id == request.UserId);
            var patient = _patientRepository.Get(p => p.UserId == userid);

            if (patient == null)
            {
                response.Errors.Add("Veri bulunamadı.");
                response.Code = "400";
                return response;
            }

            

            
            //if (doktor == null)
            //{
            //    response.Errors.Add("Doktor eklemeniz gerekmektedir");
            //    response.Code = "400";
            //    return response;
            //}

            //TODO burayı düzenle doktor bulunamazsa tanımlanamadı gibi değerler döndür
            var dr = _doctorRepository.Get(p => p.UserId == patient.DoctorId);
            if (dr == null || patient.DoctorId==0)
            {
                response.DateOfBirth = patient.DateOfBirth;
                response.DoctorId = 0;
                response.IdentityNumber = patient.IdentityNumber;
                response.Diagnosis = patient.Diagnosis;
                response.Email = user.Email;
                response.Gender = patient.Gender;
                response.Name = user.Name;
                response.Surname = user.Surname;
                response.UserStatus = patient.UserStatus;
                response.BloodGroup = patient.BloodGroup;

                response.DoctorName = "";
                response.HospitalName = "";
                response.Code = "200";
                return response;
            }

            var hospital = _hospitalRepository.Get(p => p.Id == dr.HospitalId);
            if (hospital == null)
            {
                response.Errors.Add("Doktor eklemeniz gerekmektedir.");
                response.Code = "400";
                return response;
            }

            response.DateOfBirth = patient.DateOfBirth;
            response.DoctorId = patient.DoctorId;
            response.IdentityNumber = patient.IdentityNumber;
            response.Diagnosis = patient.Diagnosis;
            response.Email = user.Email;
            response.Gender = patient.Gender;
            response.Name = user.Name;
            response.Surname = user.Surname;
            response.UserStatus = patient.UserStatus;
            var doktor = _userRepository.Get(p => p.Id == patient.DoctorId);
            if (doktor == null)
            {
                response.DoctorName = "Doktor Eklemeniz gerekmektedir";
                response.HospitalName = "Doktor Eklemeniz gerekmektedir";
            }
            else
            {
                response.DoctorName = doktor.Name + " " + doktor.Surname;
                response.HospitalName = hospital.HospitalName;
            }

            response.Message = "Hastaya ait veriler getirildi.";
            response.Code = "200";
            return response;
        }


        public PatientPutInfoResponse PutInfo(PatientPutInfoRequest request)
        {
            var response = new PatientPutInfoResponse();
            var validator = new PatientPutInfoValidator();
            var validatorResult = validator.Validate(request);

            if (!validatorResult.IsValid)
            {
                foreach (var err in validatorResult.Errors) response.Errors.Add(err.ErrorMessage);
                response.Code = "400";
                response.Errors.Add("Doğrulama Hatası");
                return response;
            }

            var userid = request.UserId;


            var patient = _patientRepository.Get(p => p.UserId == userid);
            patient.BloodGroup = request.BloodGroup;
            patient.DateOfBirth = request.DateOfBirth;
            patient.Diagnosis = request.Diagnosis;
            patient.Gender = request.Gender;
            patient.UserStatus = request.UserStatus;
            patient.IdentityNumber = request.IdentityNumber;
            _patientRepository.Update(patient);


            response.Message = "Hastaya ait veriler başarıyla değiştirildi.";
            response.Code = "200";
            return response;
        }

        public PatientQuarantineStatusResponse QuarantineStatus(PatientQuarantineStatusRequest request)
        {
            var response = new PatientQuarantineStatusResponse();

            var quarantina = _locationRepository.Get(p => p.UserId == request.UserId);

            if (quarantina == null)
            {
                response.QuarantineStatus = false;
                response.Message = "Daha onceden konum eklenmedi.";
                response.Code = "200";
                return response;
            }

            if (quarantina.QuarantineEnd > DateTime.Now)
            {
                response.QuarantineStatus = true;
                response.Message = "Karantina devam etmektedir.";
                response.Code = "200";
                return response;
            }

            response.QuarantineStatus = false;
            response.Message = "Karantina konumu değiştirilebilir.";
            response.Code = "200";
            return response;
        }

        public AddWarningResponse AddWarning(AddWarningRequest request)
        {
            var response = new AddWarningResponse();

            var warning = new Warning();
            warning.UserId = request.UserId;
            warning.WarningData = request.DataType;
            warning.WarningValue = request.WarningValue;
            warning.CreatedDate = DateTime.Now;
            warning.SaveDate = DateTime.Now;
            _warningRepository.Add(warning);
            response.Message = "200";
            response.Code = "uyarı eklendi";
            return response;
        }

        public GetHospitalResponse GetHospital()
        {
            var response = new GetHospitalResponse();

            var hospitals = _hospitalRepository.GetList();

            if (hospitals == null)
            {
                response.Code = "400";
                return response;
            }

            foreach (var hospital in hospitals) response.Hospitals.Add(hospital);

            response.Message = "Hastaneler listelendi";
            response.Code = "200";
            return response;
        }

        public GetHospitalDoctorsResponse GetDoctors(long id)
        {
            var response = new GetHospitalDoctorsResponse();

            var doctors = _doctorRepository.GetList(p => p.HospitalId == id);
            if (doctors == null)
            {
                response.Errors.Add("Doktorlar bulunamadı");
                response.Code = "400";
                return response;
            }

            foreach (var doctor in doctors)
            {
                var model = new DoctorModel();
                var user = _userRepository.Get(p => p.Id == doctor.UserId);
                model.DoctorName = user.Name;
                model.DoctorSurname = user.Surname;
                model.DoctorId = doctor.UserId;
                response.Doctors.Add(model);
            }

            response.Message = "Doktorlar listelendi";
            response.Code = "200";
            return response;
        }

        public AddCompanionResponse AddCompanion(AddCompanionRequest request)
        {
            var response = new AddCompanionResponse();
            var validator = new AddCompanionValidator();
            var validatorResult = validator.Validate(request);

            if (!validatorResult.IsValid)
            {
                foreach (var err in validatorResult.Errors) response.Errors.Add(err.ErrorMessage);

                response.Code = "400";
                response.Errors.Add("Doğrulama hatası");
                return response;
            }

            var patient = _patientRepository.Get(p => p.UserId == request.UserId);
            if (patient == null)
            {
                response.Errors.Add("Önce Hasta bilgilerinizi doldurmanız gerekmektedir.");
                response.Code = "400";
                return response;
            }

            var companion = new Companion();
            var user = _userRepository.Get(p => p.Email == request.Email);
            if (user == null)
            {
                var NewUser = new User();
                NewUser.Email = request.Email;
                NewUser.RoleId = 5;
                NewUser.EmailConfirmed = false;
                NewUser.Name = request.Name;
                NewUser.Surname = request.Surname;
                NewUser.Password = GetPasswordHash(request.Password);
                NewUser.CreatedDate = DateTime.Now;
                _userRepository.Add(NewUser);


                var newCompanion = _userRepository.Get(p => p.Email == request.Email);
                
                companion.CompanionUserId = newCompanion.Id;
                companion.PatientUserId = request.UserId;
                companion.Status = true;
                companion.CreatedDate = DateTime.Now;

                _companionRepository.Add(companion);

                var mailUser = _userRepository.Get(p => p.Email == request.Email);
                MailVerify mailVerify = new MailVerify();
                mailVerify.UserId = mailUser.Id;
                mailVerify.Guid = Guid.NewGuid().ToString();
                mailVerify.CreatedDate = DateTime.Now;
                mailVerify.DeletedDate = DateTime.Now.AddDays(1);
                _mailVerifyRepository.Add(mailVerify);
                SendVerifyEmail(request.Email, mailVerify.Guid);

                response.Message = "Refakatci Eklendi";
                response.Code = "200";
                return response;
            }

           
            if (user.RoleId == 2 || user.RoleId == 3 || user.RoleId == 4)
            {
                response.Errors.Add("hasta veya refakatçi rolündeki birini refakatçi yapmalısınız.");
                response.Code="400";
                return response;
            }

            user.RoleId = 5;
            companion.CompanionUserId = user.Id;
            companion.PatientUserId = request.UserId;
            companion.Status = true;
            companion.CreatedDate = DateTime.Now;

            _companionRepository.Add(companion);

            _userRepository.Update(user);


            response.Message = "Var olan refakatciye eklendiniz.Refakatçi var olan şifresiyle giriş yapabilirsiniz.";
            response.Code = "200";
            return response;
            
        }

        public GetCompanionResponse ListCompanion(GetCompanionRequest request)
        {
            var response = new GetCompanionResponse();

            var companions = _companionRepository.GetList(p => p.PatientUserId == request.UserId);
            foreach (var companion in companions)
            {
                var model = new GetCompanionResponse.CompanionModel();

                var user = _userRepository.Get(p => p.Id == companion.CompanionUserId);

                model.Email = user.Email;
                model.Name = user.Name;
                model.Surname = user.Surname;
                model.Userid = user.Id;

                response.CompanionModels.Add(model);
            }

            response.Message = "Refakatçiler getirildi";
            response.Code = "200";
            return response;
        }

        public DeleteCompanionResponse DeleteCompanion(DeleteCompanionRequest request)
        {
            var response = new DeleteCompanionResponse();

            var companion = _companionRepository.Get(p =>
                p.CompanionUserId == request.CompaionId && p.PatientUserId == request.UserId);

            if (companion == null)
            {
                response.Code = "400";
                response.Errors.Add("Refakatçi bulunamadı");
                return response;
            }

            _companionRepository.Delete(companion);

            response.Message = "Refakatçi çıkarıldı";
            response.Code = "200";
            return response;
        }


        public GetPromotionResponse GetPromotion(GetPromotionRequest request)
        {
            var response = new GetPromotionResponse();

            var connections = _connectionRepository.GetList(p =>
                  p.ChildUserId == request.UserId && p.RequestStatus == false && p.Status == true);

            foreach (var connection in connections)
            {
                var model = new ConnectionRequestModel();
                var supervisorName = _userRepository.Get(p => p.Id == connection.ParentUserId);
                model.Name = supervisorName.Name;
                model.Surname = supervisorName.Surname;
                model.id = connection.Id;
                response.ConnectionRequests.Add(model);
            }

            response.Code = "200";
            response.Message = "Terfi İstekleri Getirildi";
            return response;
        }

        public ConnectionAcceptRefuseResponse ConnectionAcceptRefuse(ConnectionAcceptRefuseRequest request)
        {
            var response = new ConnectionAcceptRefuseResponse();

            var connection = _connectionRepository.Get(p => p.Id == request.Id);
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
                connection.ModifiedDate = DateTime.Now;
                response.Message = "İstek reddedildi";

            }

            if (request.isAccept)
            {
                connection.Status = false;
                connection.RequestStatus = false;
                connection.ModifiedDate = DateTime.Now;
                response.Message = "İstek kabul edildi";

                var isDoctor = _doctorRepository.Get(p => p.UserId == request.UserId);
                if (isDoctor != null)
                {
                    isDoctor.UserId = request.UserId;
                    isDoctor.SupervisorId = connection.ParentUserId;
                    isDoctor.HospitalId = _supervisorRepository.Get(p => p.UserId == connection.ParentUserId).HospitalId;
                    isDoctor.CreatedDate = DateTime.Now;
                    isDoctor.Status = true;
                    _doctorRepository.Add(isDoctor);

                    var user = _userRepository.Get(p => p.Id == connection.ChildUserId);
                    user.RoleId = 2;
                    _userRepository.Update(user);

                }
                else
                {
                    var doctor = new Doctor();
                    doctor.UserId = request.UserId;
                    doctor.SupervisorId = connection.ParentUserId;
                    doctor.HospitalId = _supervisorRepository.Get(p => p.UserId == connection.ParentUserId).HospitalId;
                    doctor.CreatedDate = DateTime.Now;
                    doctor.Status = true;
                    _doctorRepository.Add(doctor);

                    var user = _userRepository.Get(p => p.Id == connection.ChildUserId);
                    user.RoleId = 2;
                    _userRepository.Update(user);
                }
            }

            _connectionRepository.Update(connection);

            response.Code = "200";
            // response.Message = "İstek kabul edildi";

            return response;
        }

        

        public GetDateRangeResponse DateRangeData(GetDateRangeRequest request)
        {
            var response = new GetDateRangeResponse();


            response.Code = "200";
            response.Message = "Hastaya ait veriler getirildi";
            return response;

        }

        public void SendVerifyEmail(string mail, string guid)
        {

            string verifyUrl = "http://covid.smartsense.com.tr/VerifyEmail/" + guid + " \n " +
                               "Bu Maili Yanlışlıkla Aldığınızı Düşünüyorsanız veya Size Ait Bir Talep Değilse Lütfen Dikkate Almayın. \n Smartsense Biyomedikal Yazılım \n www.smartsense.com.tr   ";




            MailMessage msg = new MailMessage();
            SmtpClient client = new SmtpClient();
            client.Credentials = new System.Net.NetworkCredential("sagtekyazilim@gmail.com", "Sagtek21");
            client.Port = 587;
            client.Host = "smtp.gmail.com";
            client.EnableSsl = true;
            msg.To.Add(mail);
            msg.From = new MailAddress("sagtekyazilim@gmail.com", "Smartsense Mail Doğrulaması");
            msg.Subject = "Email Doğrulaması";
            msg.Body = verifyUrl;

            client.Send(msg);


        }
        public User Get(string email)
        {
            return _userRepository.Get(p => p.Email == email);
        }

        public string GetPasswordHash(string password)
        {
            return BCrypt.Net.BCrypt.HashPassword(password);
        }
    }
}
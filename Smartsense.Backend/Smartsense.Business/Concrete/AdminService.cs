using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Smartsense.Business.Abstract;
using Smartsense.Data.Abstract;
using Smartsense.Entity.DTO;
using Smartsense.Entity.Models;
using Smartsense.Entity.Validators;

namespace Smartsense.Business.Concrete
{
    public class AdminService : IAdminService
    {
        private readonly ISupervisorRepository _supervisorRepository;
        private readonly IDoctorRepository _doctorRepository;
        private readonly IValueRepository _valueRepository;
        private readonly IPatientRepository _patientRepository;
        private readonly IUserRepository _userRepository;
        private readonly IConnectionRepository _connectionRepository;
        private readonly ILocationRepository _locationRepository;
        private readonly IWarningRepository _warningRepository;
        private readonly ILocationWarningRepository _locationWarningRepository;
        private readonly IHospitalRepository _hospitalRepository;

        public AdminService(IDoctorRepository doctorRepository, IHospitalRepository hospitalRepository,
            ISupervisorRepository supervisorRepository, IValueRepository valueRepository,
            ILocationWarningRepository locationWarningRepository, IWarningRepository warningRepository,
            IPatientRepository patientRepository, IUserRepository userRepository,
            ILocationRepository locationRepository, IConnectionRepository connectionRepository)
        {
            _doctorRepository = doctorRepository;
            _valueRepository = valueRepository;
            _patientRepository = patientRepository;
            _userRepository = userRepository;
            _connectionRepository = connectionRepository;
            _locationRepository = locationRepository;
            _warningRepository = warningRepository;
            _locationWarningRepository = locationWarningRepository;
            _supervisorRepository = supervisorRepository;
            _hospitalRepository = hospitalRepository;
        }
       
   

        public AddHospitalResponse AddHospital(AddHospitalRequest request)
        {
            var response = new AddHospitalResponse();
            var validator = new AddHospitalValidator();
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

            var user = _userRepository.Get(p => p.Id == request.UserId);
            if (user.RoleId != 4)
            {
                response.Code = "400";
                response.Errors.Add("Yetki Hatası");
                return response;
            }

            var hospital = new Hospital();
            hospital.HospitalAddress = request.Address;
            hospital.HospitalCapacity = request.HospitalCapacity;
            hospital.HospitalName = request.HospitalName;
            hospital.CreatedDate = DateTime.Now;
            hospital.Status = true;
            _hospitalRepository.Add(hospital);


            response.Code = "200";
            response.Message = "Hastane Eklendi";
            return response;
        }

        public AdminPromotionResponse SupervisorPromotion(AdminPromotionRequest request)
        {
            var response = new AdminPromotionResponse();

            var user = _userRepository.Get(p => p.Id == request.PromotionId);
            if (user == null)
            {
                response.Code = "400";
                response.Errors.Add("Böyle bir kullanıcı bulunamadı.");
                return response;
            }

            var adminuser = _userRepository.Get(p => p.Id == request.UserId);
            if (adminuser.RoleId != 4)
            {
                response.Code = "400";
                response.Errors.Add("Yetki Hatası");
                return response;
            }

            user.RoleId = 3;
            _userRepository.Update(user);//Yeni başhekim başhekim rolüne atanır

            var hospital = _supervisorRepository.Get(p => p.HospitalId == request.HospitalId);
            if (hospital !=null)//Kayıtlı bi hastanenin başhekimi değiştirilecekse
            {
               var demotionuser = _userRepository.Get(p => p.Id == hospital.UserId);
               demotionuser.RoleId = 1;
               _userRepository.Update(demotionuser);//eski başhekim kullanıcı rolüne düşürülür
               
              var doctors= _doctorRepository.GetList(p => p.SupervisorId == hospital.UserId);
              foreach (var doctor in doctors)
              {
                  doctor.SupervisorId = request.PromotionId;
                  doctor.HospitalId = request.HospitalId;
                  _doctorRepository.Update(doctor);//başhekime ait doctorların idleri güncellenir
              }


                hospital.UserId = request.PromotionId;
                _supervisorRepository.Update(hospital);//supervisor tablosu güncellenir

                response.Code = "200";
                response.Message = "Hastanenin başhakimi güncellendi";
                return response;
            }

            var supervisor = new Supervisor();
            supervisor.HospitalId = request.HospitalId;
            supervisor.UserId = request.PromotionId;
            _supervisorRepository.Add(supervisor);

            response.Code = "200";
            response.Message = "Supervisor rolüne getirildi";
            return response;
        }

        public AdminDemotionResponse SupervisorDemotion(AdminDemotionRequest request)
        {
            var response = new AdminDemotionResponse();


            response.Code = "200";
            response.Message = "Supervisor rolüne getirildi";
            return response;
        }

        public GetAllHospitalResponse GetAllHospital(GetAllHospitalRequest request)
        {
            var response = new GetAllHospitalResponse();

            var user = _userRepository.Get(p => p.Id == request.UserId);
            if (user.RoleId != 4)
            {
                response.Code = "400";
                response.Errors.Add("Yetki Hatası");
                return response;
            }

            var hospitals = _hospitalRepository.GetList();

            foreach (var h in hospitals)
            {
                var hospital = new Hospital();
                hospital.HospitalAddress = h.HospitalAddress;
                hospital.HospitalCapacity = h.HospitalCapacity;
                hospital.HospitalName = h.HospitalName;
                hospital.Id = h.Id;
                response.Hospitals.Add(hospital);
            }

            response.Code = "200";
            response.Message = "Hastaneler listelendi.";
            return response;
        }

        public GetAllUsersResponse GetAllUsers(GetAllUsersRequest request)
        {
            var response = new GetAllUsersResponse();

            var admin = _userRepository.Get(p => p.Id == request.UserId);
            if (admin.RoleId != 4)
            {
                response.Code = "400";
                response.Errors.Add("Yetki Hatası");
                return response;
            }

            var users = _userRepository.GetList();

            foreach (var user in users)
            {
                var model = new UsersResponseModel();

                model.Id = user.Id;
                model.Name = user.Name;
                model.SurName = user.Surname;
                model.Email = user.Email;
                model.EmailConfirmed = user.EmailConfirmed;
                model.Phone = user.Phone;
                model.RoleId = user.RoleId;
                response.UsersList.Add(model);
            }

            response.Code = "200";
            response.Message = "Kullanıcılar listelendi.";
            return response;
        }
    }
}
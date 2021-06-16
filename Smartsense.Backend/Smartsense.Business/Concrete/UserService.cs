using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Net.Mail;
using Smartsense.Data.Abstract;
using Microsoft.EntityFrameworkCore.Query;
using Smartsense.Business.Abstract;

using Smartsense.Entity.DTO;
using Smartsense.Entity.Enums;
using Smartsense.Entity.Models;
using Smartsense.Entity.Validators;

namespace Smartsense.Business.Concrete
{
    public class UserService : IUserService
    {
        private readonly IUserRepository _userRepository;
        private readonly IMailVerifyRepository _mailVerifyRepository;
        private readonly IRoleRepository _roleRepository;
        private readonly IUserRoleRepository _userRoleRepository;
        private readonly IPasswordResetRepository _passwordResetRepository;
        private readonly IConnectionRepository _connectionRepository;
        public UserService(IUserRepository userRepository, IConnectionRepository connectionRepository, IMailVerifyRepository mailVerifyRepository, IPasswordResetRepository passwordResetRepository,
            IRoleRepository roleRepository ,IUserRoleRepository userRoleRepository)
        {
            _userRepository = userRepository;
            _mailVerifyRepository = mailVerifyRepository;
            _roleRepository = roleRepository;
            _userRoleRepository = userRoleRepository;
            _passwordResetRepository = passwordResetRepository;
            _connectionRepository = connectionRepository;
        }

        public UserLoginResponse Login(UserLoginRequest request)
        {
            var response = new UserLoginResponse();

            var validator = new LoginValidator();
            var validatorResult = validator.Validate(request);

            if (!validatorResult.IsValid)
            {
                foreach (var err in validatorResult.Errors)
                {
                    response.Errors.Add(err.ErrorMessage);
                }

                response.Code = "400";
                response.Errors.Add("Doğrulama hatası");
                return response;
            }

            var user = Get(request.Email);
            if (user != null && !user.EmailConfirmed)
            {
                return GetLoginResponse("400",
                    "Mailinizi onaylamanız gerekmektedir. Lütfen maillerinizi kontrol ediniz.");
            }
            
            if (user == null)
            {
                return GetLoginResponse("400", "Böyle bir kullanıcı bulunamadı.");
            }

            //Sonra değişecek
            if (!VerifyPassword(request.Password, user.Password))
            {
                return GetLoginResponse("400", "Şifre yanlış");
            }


            var userLoginResponse = new UserLoginResponse();
            userLoginResponse.Name = user.Name;
            userLoginResponse.Surname = user.Surname;
            userLoginResponse.Phone = user.Phone;
            userLoginResponse.UserId = user.Id;
            userLoginResponse.RoleId = user.RoleId;
            userLoginResponse.Code = "200";
            userLoginResponse.Message = "Giriş başarılı";
            userLoginResponse.UserId = user.Id;
            return userLoginResponse;


        }


        public UserRegisterResponse Register(UserRegisterRequest request)
        {
            var response = new UserRegisterResponse();

            var validator = new RegisterValidator();
            var validatorResult = validator.Validate(request);

            if (!validatorResult.IsValid)
            {
                foreach (var err in validatorResult.Errors)
                {
                    response.Errors.Add(err.ErrorMessage);
                }

                response.Code = "400";
                response.Errors.Add("Doğrulama hatası");
                return response;
            }

            var user = Get(request.Email);

            if (user != null)
            {
                return GetRegisterResponse("400", "Bu email adresine kayıtlı kullanıcı bulunmaktadır.");
            }

            var mailUser = _userRepository.Add(SetUserDefualtData(request));
            MailVerify mailVerify = new MailVerify();
            mailVerify.UserId = mailUser.Id;
            mailVerify.Guid = Guid.NewGuid().ToString();
            mailVerify.CreatedDate = DateTime.Now;
            mailVerify.DeletedDate = DateTime.Now.AddDays(1);
            _mailVerifyRepository.Add(mailVerify);
            SendVerifyEmail(mailUser.Email, mailVerify.Guid);

            return GetRegisterResponse("200", "Kullanıcı kaydı başarılı.");
        }


      
        public User Get(string email, string password)
        {
            return _userRepository.Get(p => p.Email == email && p.Password == password);
        }

        public User Get(string email)
        {
            return _userRepository.Get(p => p.Email == email);
        }
        public User Get(long id)
        {
            return _userRepository.Get(p => p.Id == id);
        }

        public RefreshTokenResponse RefreshToken(RefreshTokenRequest request)
        {
            var response = new RefreshTokenResponse();

            var user = Get(request.Email);
            if (user == null)
            {
                
                response.Code = "400";
                response.Errors.Add("Böyle bir kullanıcı bulunamadı.");
                return response;
            }
            if (!VerifyPassword(request.Password, user.Password))
            {
                response.Code = "400";
                response.Errors.Add("Şifre yanlış");
                return response;
                
            }

            return response;
        }


        

        public string GetPasswordHash(string password)
        {
            return BCrypt.Net.BCrypt.HashPassword(password);
        }

        public bool VerifyPassword(string password, string passwordHash)
        {
            return BCrypt.Net.BCrypt.Verify(password, passwordHash);
        }

        public UserRegisterResponse GetRegisterResponse(string code, string message)
        {
            var userRegisterResponse = new UserRegisterResponse();
            userRegisterResponse.Code = code;
            if (code.Equals("400"))
            {
                userRegisterResponse.Errors.Add(message);
            }
            else
            {
                userRegisterResponse.Message = message;
            }
            return userRegisterResponse;
        }

        public UserLoginResponse GetLoginResponse(string code, string message)
        {
            var userLoginResponse = new UserLoginResponse();
            userLoginResponse.Code = code;
            if (code.Equals("400"))
            {
                userLoginResponse.Errors.Add(message);
            }
            else
            {
                userLoginResponse.Message = message;
            }

            return userLoginResponse;
        }

        public User SetUserDefualtData(UserRegisterRequest request)
        {
            var user = new User();
            user.Email = request.Email;
            user.Name = request.Name;
            user.Surname = request.Surname;
            user.Password = GetPasswordHash(request.Password);
            user.EmailConfirmed = false;
            user.Ip = request.Ip;
            user.CreatedDate = DateTime.Now;
            user.Status =true;
            user.RoleId = 1;
            return user;
        }
        public MailVerifyResponse Verify(MailVerifyRequest request)
        {
            var mailVerifyResponse = new MailVerifyResponse();
            var verify = GetGuid(request.Guid);
            if (verify == null)
            {
                mailVerifyResponse.Code = "400";
                mailVerifyResponse.Errors.Add("Doğrulama işleminde hata meydana geldi.");
                return mailVerifyResponse;
            }

            if (verify.Status)
            {
                mailVerifyResponse.Code = "200";
                mailVerifyResponse.Message = "Mail adresiniz önceden doğrulanmıştır.";
                return mailVerifyResponse;
            }

            if (verify.DeletedDate <= DateTime.Now)
            {
                mailVerifyResponse.Code = "400";
                mailVerifyResponse.Errors.Add("Doğrulama süresi geçmiştir.");
                return mailVerifyResponse;
            }

            var user = Get(verify.UserId);
            if (user == null)
            {
                mailVerifyResponse.Code = "400";
                mailVerifyResponse.Errors.Add("Kullanıcı bulunamadı.");
                return mailVerifyResponse;
            }

            user.EmailConfirmed = true;
            _userRepository.Update(user);

            verify.ModifiedDate = DateTime.Now;
            verify.Status = true;
            _mailVerifyRepository.Update(verify);

            mailVerifyResponse.Code = "200";
            mailVerifyResponse.Message = "Email doğrulama başarıyla yapıldı. Giriş yapabilirsiniz.";
            return mailVerifyResponse;

        }

        public UserPutInfoResponse PutInfo(UserPutInfoRequest request)
        {
          var user=  _userRepository.Get(p => p.Id == request.UserId);
          var response = new UserPutInfoResponse();
            UserPutInfoValidator validator = new UserPutInfoValidator();
          var validatorResult = validator.Validate(request);
            if (!validatorResult.IsValid)
          {
              foreach (var err in validatorResult.Errors)
              {
                  response.Errors.Add(err.ErrorMessage);
              }

              response.Code = "400";
              response.Errors.Add("Doğrulama hatası");
              return response;
          }


         user.Name = request.Name;
          user.Surname = request.Surname;
          user.Phone = request.Phone;
          user.ModifiedDate=DateTime.Now;

          _userRepository.Update(user);

         
          response.Name = user.Name;
          response.Surname = user.Surname;
          response.Phone = user.Phone; 
          response.UserId = user.Id;
          response.Code = "200";
          response.Message = "Veriler güncellendi";
         
          return response;


        }

        public UserGetInfoResponse GetInfo(UserGetInfoRequest request)
        {
            var user = _userRepository.Get(p => p.Id == request.UserId);
            var response = new UserGetInfoResponse();
            response.Name = user.Name;
            response.Surname = user.Surname;
            response.Phone = user.Phone;
            response.UserId = user.Id;
            response.Code = "200";
            response.Message = "Veriler getirildi";
            return response;
        }

        public PasswordForgotResponse PasswordForgot(PasswordForgotRequest request)
        {
            var response = new PasswordForgotResponse();
            var validator = new PasswordForgotValidator();
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

            var user = _userRepository.Get(p => p.Email == request.Email);
            if (user == null)
            {
                response.Code = "200";
                response.Message = "Sıfırlama bağlantısı mail adresinize gönderilmiştir";
                return response;
            }

            var reset = new PasswordReset();
            reset.UserId = user.Id;
            reset.Guid= Guid.NewGuid().ToString();
            reset.CreatedDate=DateTime.Now;
            reset.DeletedDate = DateTime.Now.AddDays(1);

            _passwordResetRepository.Add(reset);
            
            SendResetEmail(request.Email,reset.Guid);

            response.Code = "200";
            response.Message = "Sıfırlama bağlantısı mail adresinize gönderilmiştir";
            return response;
        }

        public PasswordResetResponse PasswordReset(PasswordResetRequest request)
        {
            var response =new  PasswordResetResponse();
            var reset =_passwordResetRepository.Get(p => p.Guid == request.Guid);
            if (reset.DeletedDate < DateTime.Now)
            {
                response.Code = "400";
                response.Errors.Add("Şifre sıfırlama süresi geçmiştir.");
            }

            var user = _userRepository.Get(p => p.Id == reset.UserId);

            if (string.Compare(request.Password, request.ConfirmPassword )!=0)
            {
                response.Code = "400";
                response.Errors.Add("Şifreler uyuşmuyor");
                return response;
            }
            user.Password=GetPasswordHash(request.Password);

            _userRepository.Update(user);
            reset.ModifiedDate=DateTime.Now;
            _passwordResetRepository.Update(reset);
            ChangedPasswordEmail(user.Email);

            response.Code = "200";
            response.Message = "Şifreniz sıfırlanmıştır";
            return response;
        }

        public ChangePasswordResponse ChangePassword(ChangePasswordRequest request)
        {
            var response = new ChangePasswordResponse();
            var validator = new ChangePasswordValidator();
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

            if (user == null)
            {
                response.Code = "400";
                response.Errors.Add("Kullanıcı bulunamadı.");
                return response;
            }

            if (!VerifyPassword(request.OldPassword, user.Password))
            {
                response.Code = "400";
                response.Errors.Add("Eski şifrenizi doğru girdiğinizden emin olunuz");
                return response;
            }

            if (request.NewPassword != request.ConfirmPassword)
            {
                response.Code = "400";
                response.Errors.Add("Şifreler uyuşmamaktadır");
                return response;
            }

            user.Password = GetPasswordHash(request.NewPassword);

            _userRepository.Update(user);

            ChangedPasswordEmail(user.Email);
            response.Code = "200";
            response.Message = "Şifreniz değiştirilmiştir";
            return response;
        }

       

        public MailVerify GetGuid(string guid)
        {
            return _mailVerifyRepository.Get(p => p.Guid == guid && p.DeletedDate > DateTime.Now);
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
        public void SendResetEmail(string mail, string guid)
        {

            string verifyUrl = "http://covid.smartsense.com.tr/ResetPassword/" + guid + " \n Şifre Sıfırlama Talebi Size Ait Değilse Lütfen Linke Tıklamayın."+ " \n " + " Bu Maili Yanlışlıkla Aldığınızı Düşünüyorsanız veya Size Ait Bir Talep Değilse Lütfen Dikkate Almayın. \n Smartsense Biyomedikal Yazılım \n www.smartsense.com.tr ";

            MailMessage msg = new MailMessage();
            SmtpClient client = new SmtpClient();
            client.Credentials = new System.Net.NetworkCredential("sagtekyazilim@gmail.com", "Sagtek21");
            client.Port = 587;
            client.Host = "smtp.gmail.com";
            client.EnableSsl = true;
            msg.To.Add(mail);
            msg.From = new MailAddress("sagtekyazilim@gmail.com", "Smartsense Şifre Sıfırlama");
            msg.Subject = "Şifre Sıfırlama";
            msg.Body = verifyUrl;

            client.Send(msg);


        }
        public void ChangedPasswordEmail(string mail)
        {

            string verifyUrl = "Mail Adresinize Ait Şifre Değiştirilmiştir \n Şifre Değiştirilme İşlemi Size Ait Değilse Lütfen Destek İle İletişime Geçin."+"\n Smartsense Biyomedikal Yazılım \n www.smartsense.com.tr ";

            MailMessage msg = new MailMessage();
            SmtpClient client = new SmtpClient();
            client.Credentials = new System.Net.NetworkCredential("sagtekyazilim@gmail.com", "Sagtek21");
            client.Port = 587;
            client.Host = "smtp.gmail.com";
            client.EnableSsl = true;
            msg.To.Add(mail);
            msg.From = new MailAddress("sagtekyazilim@gmail.com", "Smartsense Şifre Değiştirilme İşlemi");
            msg.Subject = "Şifre Değiştirilme İşlemi";
            msg.Body = verifyUrl;

            client.Send(msg);


        }
    }
}
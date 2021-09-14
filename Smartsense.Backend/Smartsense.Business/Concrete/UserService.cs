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

            
                string verifyUrl = "http://hasta.smartsense.com.tr/VerifyEmail/" + guid;
                string myString = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n";
                myString = myString + "\n";
                myString = myString + "<head>\n";
                myString = myString + "	<!--[if gte mso 9]><xml><o:OfficeDocumentSettings><o:AllowPNG/><o:PixelsPerInch>96</o:PixelsPerInch></o:OfficeDocumentSettings></xml><![endif]-->\n";
                myString = myString + "	<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n";
                myString = myString + "	<meta name=\"viewport\" content=\"width=device-width\">\n";
                myString = myString + "	<!--[if !mso]><!-->\n";
                myString = myString + "	<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n";
                myString = myString + "	<!--<![endif]-->\n";
                myString = myString + "	<title></title>\n";
                myString = myString + "	<!--[if !mso]><!-->\n";
                myString = myString + "	<link href=\"https://fonts.googleapis.com/css?family=Montserrat\" rel=\"stylesheet\" type=\"text/css\">\n";
                myString = myString + "	<!--<![endif]-->\n";
                myString = myString + "	<style type=\"text/css\">\n";
                myString = myString + "		body {\n";
                myString = myString + "			margin: 0;\n";
                myString = myString + "			padding: 0;\n";
                myString = myString + "		}\n";
                myString = myString + "\n";
                myString = myString + "		table,\n";
                myString = myString + "		td,\n";
                myString = myString + "		tr {\n";
                myString = myString + "			vertical-align: top;\n";
                myString = myString + "			border-collapse: collapse;\n";
                myString = myString + "		}\n";
                myString = myString + "\n";
                myString = myString + "		* {\n";
                myString = myString + "			line-height: inherit;\n";
                myString = myString + "		}\n";
                myString = myString + "\n";
                myString = myString + "		a[x-apple-data-detectors=true] {\n";
                myString = myString + "			color: inherit !important;\n";
                myString = myString + "			text-decoration: none !important;\n";
                myString = myString + "		}\n";
                myString = myString + "	</style>\n";
                myString = myString + "	<style type=\"text/css\" id=\"media-query\">\n";
                myString = myString + "		@media (max-width: 660px) {\n";
                myString = myString + "\n";
                myString = myString + "			.block-grid,\n";
                myString = myString + "			.col {\n";
                myString = myString + "				min-width: 320px !important;\n";
                myString = myString + "				max-width: 100% !important;\n";
                myString = myString + "				display: block !important;\n";
                myString = myString + "			}\n";
                myString = myString + "\n";
                myString = myString + "			.block-grid {\n";
                myString = myString + "				width: 100% !important;\n";
                myString = myString + "			}\n";
                myString = myString + "\n";
                myString = myString + "			.col {\n";
                myString = myString + "				width: 100% !important;\n";
                myString = myString + "			}\n";
                myString = myString + "\n";
                myString = myString + "			.col_cont {\n";
                myString = myString + "				margin: 0 auto;\n";
                myString = myString + "			}\n";
                myString = myString + "\n";
                myString = myString + "			img.fullwidth,\n";
                myString = myString + "			img.fullwidthOnMobile {\n";
                myString = myString + "				width: 100% !important;\n";
                myString = myString + "			}\n";
                myString = myString + "\n";
                myString = myString + "			.no-stack .col {\n";
                myString = myString + "				min-width: 0 !important;\n";
                myString = myString + "				display: table-cell !important;\n";
                myString = myString + "			}\n";
                myString = myString + "\n";
                myString = myString + "			.no-stack.two-up .col {\n";
                myString = myString + "				width: 50% !important;\n";
                myString = myString + "			}\n";
                myString = myString + "\n";
                myString = myString + "			.no-stack .col.num2 {\n";
                myString = myString + "				width: 16.6% !important;\n";
                myString = myString + "			}\n";
                myString = myString + "\n";
                myString = myString + "			.no-stack .col.num3 {\n";
                myString = myString + "				width: 25% !important;\n";
                myString = myString + "			}\n";
                myString = myString + "\n";
                myString = myString + "			.no-stack .col.num4 {\n";
                myString = myString + "				width: 33% !important;\n";
                myString = myString + "			}\n";
                myString = myString + "\n";
                myString = myString + "			.no-stack .col.num5 {\n";
                myString = myString + "				width: 41.6% !important;\n";
                myString = myString + "			}\n";
                myString = myString + "\n";
                myString = myString + "			.no-stack .col.num6 {\n";
                myString = myString + "				width: 50% !important;\n";
                myString = myString + "			}\n";
                myString = myString + "\n";
                myString = myString + "			.no-stack .col.num7 {\n";
                myString = myString + "				width: 58.3% !important;\n";
                myString = myString + "			}\n";
                myString = myString + "\n";
                myString = myString + "			.no-stack .col.num8 {\n";
                myString = myString + "				width: 66.6% !important;\n";
                myString = myString + "			}\n";
                myString = myString + "\n";
                myString = myString + "			.no-stack .col.num9 {\n";
                myString = myString + "				width: 75% !important;\n";
                myString = myString + "			}\n";
                myString = myString + "\n";
                myString = myString + "			.no-stack .col.num10 {\n";
                myString = myString + "				width: 83.3% !important;\n";
                myString = myString + "			}\n";
                myString = myString + "\n";
                myString = myString + "			.video-block {\n";
                myString = myString + "				max-width: none !important;\n";
                myString = myString + "			}\n";
                myString = myString + "\n";
                myString = myString + "			.mobile_hide {\n";
                myString = myString + "				min-height: 0px;\n";
                myString = myString + "				max-height: 0px;\n";
                myString = myString + "				max-width: 0px;\n";
                myString = myString + "				display: none;\n";
                myString = myString + "				overflow: hidden;\n";
                myString = myString + "				font-size: 0px;\n";
                myString = myString + "			}\n";
                myString = myString + "\n";
                myString = myString + "			.desktop_hide {\n";
                myString = myString + "				display: block !important;\n";
                myString = myString + "				max-height: none !important;\n";
                myString = myString + "			}\n";
                myString = myString + "\n";
                myString = myString + "			.img-container.big img {\n";
                myString = myString + "				width: auto !important;\n";
                myString = myString + "			}\n";
                myString = myString + "		}\n";
                myString = myString + "	</style>\n";
                myString = myString + "</head>\n";
                myString = myString + "\n";
                myString = myString + "<body class=\"clean-body\" style=\"margin: 0; padding: 0; -webkit-text-size-adjust: 100%; background-color: #ececec;\">\n";
                myString = myString + "	<!--[if IE]><div class=\"ie-browser\"><![endif]-->\n";
                myString = myString + "	<table class=\"nl-container\" style=\"table-layout: fixed; vertical-align: top; min-width: 320px; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ececec; width: 100%;\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" width=\"100%\" bgcolor=\"#ececec\" valign=\"top\">\n";
                myString = myString + "		<tbody>\n";
                myString = myString + "			<tr style=\"vertical-align: top;\" valign=\"top\">\n";
                myString = myString + "				<td style=\"word-break: break-word; vertical-align: top;\" valign=\"top\">\n";
                myString = myString + "					<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" style=\"background-color:#ececec\"><![endif]-->\n";
                myString = myString + "					<div style=\"background-color:transparent;\">\n";
                myString = myString + "						<div class=\"block-grid \" style=\"min-width: 320px; max-width: 640px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; Margin: 0 auto; background-color: #00695c;\">\n";
                myString = myString + "							<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:#00695c;\">\n";
                myString = myString + "								<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background-color:transparent;\"><tr><td align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:640px\"><tr class=\"layout-full-width\" style=\"background-color:#00695c\"><![endif]-->\n";
                myString = myString + "								<!--[if (mso)|(IE)]><td align=\"center\" width=\"640\" style=\"background-color:#00695c;width:640px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top:0px; padding-bottom:0px;\"><![endif]-->\n";
                myString = myString + "								<div class=\"col num12\" style=\"min-width: 320px; max-width: 640px; display: table-cell; vertical-align: top; width: 640px;\">\n";
                myString = myString + "									<div class=\"col_cont\" style=\"width:100% !important;\">\n";
                myString = myString + "										<!--[if (!mso)&(!IE)]><!-->\n";
                myString = myString + "										<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n";
                myString = myString + "											<!--<![endif]-->\n";
                myString = myString + "											<div class=\"img-container center fixedwidth\" align=\"center\" style=\"padding-right: 0px;padding-left: 0px;\">\n";
                myString = myString + "												<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr style=\"line-height:0px\"><td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\"><![endif]-->\n";
                myString = myString + "												<div style=\"font-size:1px;line-height:20px\">&nbsp;</div><a href=\"https://www.insense.com.tr/\" target=\"_blank\" style=\"outline:none\" tabindex=\"-1\"><img class=\"center fixedwidth\" align=\"center\" border=\"0\" src=\"https://www.insense.com.tr/letter2.png\" alt=\"Image of lock &amp; key.\" title=\"Image of lock &amp; key.\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; height: auto; border: 0; width: 128px; max-width: 100%; display: block;\" width=\"128\"></a>\n";
                myString = myString + "												<div style=\"font-size:1px;line-height:20px\">&nbsp;</div>\n";
                myString = myString + "												<!--[if mso]></td></tr></table><![endif]-->\n";
                myString = myString + "											</div>\n";
                myString = myString + "											<!--[if (!mso)&(!IE)]><!-->\n";
                myString = myString + "										</div>\n";
                myString = myString + "										<!--<![endif]-->\n";
                myString = myString + "									</div>\n";
                myString = myString + "								</div>\n";
                myString = myString + "								<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n";
                myString = myString + "								<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n";
                myString = myString + "							</div>\n";
                myString = myString + "						</div>\n";
                myString = myString + "					</div>\n";
                myString = myString + "					<div style=\"background-color:transparent;\">\n";
                myString = myString + "						<div class=\"block-grid \" style=\"min-width: 320px; max-width: 640px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; Margin: 0 auto; background-color: transparent;\">\n";
                myString = myString + "							<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n";
                myString = myString + "								<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background-color:transparent;\"><tr><td align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:640px\"><tr class=\"layout-full-width\" style=\"background-color:transparent\"><![endif]-->\n";
                myString = myString + "								<!--[if (mso)|(IE)]><td align=\"center\" width=\"640\" style=\"background-color:transparent;width:640px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top:0px; padding-bottom:0px;\"><![endif]-->\n";
                myString = myString + "								<div class=\"col num12\" style=\"min-width: 320px; max-width: 640px; display: table-cell; vertical-align: top; width: 640px;\">\n";
                myString = myString + "									<div class=\"col_cont\" style=\"width:100% !important;\">\n";
                myString = myString + "										<!--[if (!mso)&(!IE)]><!-->\n";
                myString = myString + "										<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n";
                myString = myString + "											<!--<![endif]-->\n";
                myString = myString + "											<table class=\"divider\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" role=\"presentation\" valign=\"top\">\n";
                myString = myString + "												<tbody>\n";
                myString = myString + "													<tr style=\"vertical-align: top;\" valign=\"top\">\n";
                myString = myString + "														<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 30px; padding-right: 0px; padding-bottom: 0px; padding-left: 0px;\" valign=\"top\">\n";
                myString = myString + "															<table class=\"divider_content\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-top: 0px solid #BBBBBB; width: 100%;\" align=\"center\" role=\"presentation\" valign=\"top\">\n";
                myString = myString + "																<tbody>\n";
                myString = myString + "																	<tr style=\"vertical-align: top;\" valign=\"top\">\n";
                myString = myString + "																		<td style=\"word-break: break-word; vertical-align: top; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\"><span></span></td>\n";
                myString = myString + "																	</tr>\n";
                myString = myString + "																</tbody>\n";
                myString = myString + "															</table>\n";
                myString = myString + "														</td>\n";
                myString = myString + "													</tr>\n";
                myString = myString + "												</tbody>\n";
                myString = myString + "											</table>\n";
                myString = myString + "											<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 10px; padding-left: 10px; padding-top: 10px; padding-bottom: 10px; font-family: 'Trebuchet MS', Tahoma, sans-serif\"><![endif]-->\n";
                myString = myString + "											<div style=\"color:#ffffff;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:1.2;padding-top:10px;padding-right:10px;padding-bottom:10px;padding-left:10px;\">\n";
                myString = myString + "												<div class=\"txtTinyMce-wrapper\" style=\"line-height: 1.2; font-size: 12px; font-family: 'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #ffffff; mso-line-height-alt: 14px;\">\n";
                myString = myString + "													<p style=\"margin: 0; font-size: 20px; line-height: 1.2; text-align: center; word-break: break-word; mso-line-height-alt: 24px; margin-top: 0; margin-bottom: 0;\"><span style=\"font-size: 20px; color: #555555;\"><strong>Merhaba, üyeli&#287;iniz olu&#351;turuldu. Lütfen hesab&#305;n&#305;z&#305; do&#287;rulay&#305;n&#305;z.</strong></span></p>\n";
                myString = myString + "												</div>\n";
                myString = myString + "											</div>\n";
                myString = myString + "											<!--[if mso]></td></tr></table><![endif]-->\n";
                myString = myString + "											<div class=\"button-container\" align=\"center\" style=\"padding-top:20px;padding-right:10px;padding-bottom:20px;padding-left:10px;\">\n";
                myString = myString + "												<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-spacing: 0; border-collapse: collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;\"><tr><td style=\"padding-top: 20px; padding-right: 10px; padding-bottom: 20px; padding-left: 10px\" align=\"center\"><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" href=\""+verifyUrl+"\" style=\"height:46.5pt;width:98.25pt;v-text-anchor:middle;\" arcsize=\"57%\" stroke=\"false\" fillcolor=\"#00695c\"><w:anchorlock/><v:textbox inset=\"0,0,0,0\"><center style=\"color:#ffffff; font-family:'Trebuchet MS', Tahoma, sans-serif; font-size:16px\"><![endif]--><a href=\""+verifyUrl+"\" target=\"_blank\" style=\"-webkit-text-size-adjust: none; text-decoration: none; display: inline-block; color: #ffffff; background-color: #00695c; border-radius: 35px; -webkit-border-radius: 35px; -moz-border-radius: 35px; width: auto; width: auto; border-top: 1px solid #00695c; border-right: 1px solid #00695c; border-bottom: 1px solid #00695c; border-left: 1px solid #00695c; padding-top: 15px; padding-bottom: 15px; font-family: 'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; text-align: center; mso-border-alt: none; word-break: keep-all;\"><span style=\"padding-left:30px;padding-right:30px;font-size:16px;display:inline-block;letter-spacing:undefined;\"><span style=\"font-size: 16px; margin: 0; line-height: 2; word-break: break-word; mso-line-height-alt: 32px;\"><span style=\"font-size: 16px; line-height: 32px;\" data-mce-style=\"font-size: 16px; line-height: 32px;\"><strong>DO&#286;RULA</strong></span></span></span></a>\n";
                myString = myString + "												<!--[if mso]></center></v:textbox></v:roundrect></td></tr></table><![endif]-->\n";
                myString = myString + "											</div>\n";
                myString = myString + "											<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 10px; padding-left: 10px; padding-top: 10px; padding-bottom: 10px; font-family: 'Trebuchet MS', Tahoma, sans-serif\"><![endif]-->\n";
                myString = myString + "											<div style=\"color:#ffffff;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:1.5;padding-top:10px;padding-right:10px;padding-bottom:10px;padding-left:10px;\">\n";
                myString = myString + "												<div class=\"txtTinyMce-wrapper\" style=\"line-height: 1.5; font-size: 12px; font-family: 'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #ffffff; mso-line-height-alt: 18px;\">\n";
                myString = myString + "													<p style=\"margin: 0; font-size: 15px; line-height: 1.5; text-align: center; word-break: break-word; mso-line-height-alt: 23px; margin-top: 0; margin-bottom: 0;\"><span style=\"color: #555555; font-size: 15px;\">Bu e-maili yanl&#305;&#351;l&#305;kla ald&#305;&#287;&#305;n&#305;z&#305; dü&#351;ünüyorsan&#305;z ya da size ait bir talep de&#287;ilse lütfen dikkate almay&#305;n&#305;z.&nbsp;</span></p>\n";
                myString = myString + "												</div>\n";
                myString = myString + "											</div>\n";
                myString = myString + "											<!--[if mso]></td></tr></table><![endif]-->\n";
                myString = myString + "											<table class=\"divider\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" role=\"presentation\" valign=\"top\">\n";
                myString = myString + "												<tbody>\n";
                myString = myString + "													<tr style=\"vertical-align: top;\" valign=\"top\">\n";
                myString = myString + "														<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 30px; padding-right: 0px; padding-bottom: 0px; padding-left: 0px;\" valign=\"top\">\n";
                myString = myString + "															<table class=\"divider_content\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-top: 0px solid #BBBBBB; width: 100%;\" align=\"center\" role=\"presentation\" valign=\"top\">\n";
                myString = myString + "																<tbody>\n";
                myString = myString + "																	<tr style=\"vertical-align: top;\" valign=\"top\">\n";
                myString = myString + "																		<td style=\"word-break: break-word; vertical-align: top; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\"><span></span></td>\n";
                myString = myString + "																	</tr>\n";
                myString = myString + "																</tbody>\n";
                myString = myString + "															</table>\n";
                myString = myString + "														</td>\n";
                myString = myString + "													</tr>\n";
                myString = myString + "												</tbody>\n";
                myString = myString + "											</table>\n";
                myString = myString + "											<!--[if (!mso)&(!IE)]><!-->\n";
                myString = myString + "										</div>\n";
                myString = myString + "										<!--<![endif]-->\n";
                myString = myString + "									</div>\n";
                myString = myString + "								</div>\n";
                myString = myString + "								<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n";
                myString = myString + "								<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n";
                myString = myString + "							</div>\n";
                myString = myString + "						</div>\n";
                myString = myString + "					</div>\n";
                myString = myString + "					<div style=\"background-color:transparent;\">\n";
                myString = myString + "						<div class=\"block-grid \" style=\"min-width: 320px; max-width: 640px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; Margin: 0 auto; background-color: transparent;\">\n";
                myString = myString + "							<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n";
                myString = myString + "								<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background-color:transparent;\"><tr><td align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:640px\"><tr class=\"layout-full-width\" style=\"background-color:transparent\"><![endif]-->\n";
                myString = myString + "								<!--[if (mso)|(IE)]><td align=\"center\" width=\"640\" style=\"background-color:transparent;width:640px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top:0px; padding-bottom:0px;\"><![endif]-->\n";
                myString = myString + "								<div class=\"col num12\" style=\"min-width: 320px; max-width: 640px; display: table-cell; vertical-align: top; width: 640px;\">\n";
                myString = myString + "									<div class=\"col_cont\" style=\"width:100% !important;\">\n";
                myString = myString + "										<!--[if (!mso)&(!IE)]><!-->\n";
                myString = myString + "										<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n";
                myString = myString + "											<!--<![endif]-->\n";
                myString = myString + "											<div class=\"img-container center fixedwidth\" align=\"center\" style=\"padding-right: 0px;padding-left: 0px;\">\n";
                myString = myString + "												<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr style=\"line-height:0px\"><td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\"><![endif]--><a href=\"https://www.smartsense.com.tr/\" target=\"_blank\" style=\"outline:none\" tabindex=\"-1\"><img class=\"center fixedwidth\" align=\"center\" border=\"0\" src=\"https://www.smartsense.com.tr/img/ss-logo.PNG\" alt=\"Smartsense Logo\" title=\"Smartsense Logo\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; height: auto; border: 0; width: 160px; max-width: 100%; display: block;\" width=\"160\"></a>\n";
                myString = myString + "												<!--[if mso]></td></tr></table><![endif]-->\n";
                myString = myString + "											</div>\n";
                myString = myString + "											<!--[if (!mso)&(!IE)]><!-->\n";
                myString = myString + "										</div>\n";
                myString = myString + "										<!--<![endif]-->\n";
                myString = myString + "									</div>\n";
                myString = myString + "								</div>\n";
                myString = myString + "								<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n";
                myString = myString + "								<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n";
                myString = myString + "							</div>\n";
                myString = myString + "						</div>\n";
                myString = myString + "					</div>\n";
                myString = myString + "					<div style=\"background-color:transparent;\">\n";
                myString = myString + "						<div class=\"block-grid \" style=\"min-width: 320px; max-width: 640px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; Margin: 0 auto; background-color: #00695c;\">\n";
                myString = myString + "							<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:#00695c;\">\n";
                myString = myString + "								<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background-color:transparent;\"><tr><td align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:640px\"><tr class=\"layout-full-width\" style=\"background-color:#00695c\"><![endif]-->\n";
                myString = myString + "								<!--[if (mso)|(IE)]><td align=\"center\" width=\"640\" style=\"background-color:#00695c;width:640px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top:0px; padding-bottom:0px;\"><![endif]-->\n";
                myString = myString + "								<div class=\"col num12\" style=\"min-width: 320px; max-width: 640px; display: table-cell; vertical-align: top; width: 640px;\">\n";
                myString = myString + "									<div class=\"col_cont\" style=\"width:100% !important;\">\n";
                myString = myString + "										<!--[if (!mso)&(!IE)]><!-->\n";
                myString = myString + "										<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n";
                myString = myString + "											<!--<![endif]-->\n";
                myString = myString + "											<table class=\"social_icons\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" valign=\"top\">\n";
                myString = myString + "												<tbody>\n";
                myString = myString + "													<tr style=\"vertical-align: top;\" valign=\"top\">\n";
                myString = myString + "														<td style=\"word-break: break-word; vertical-align: top; padding-top: 30px; padding-right: 10px; padding-bottom: 10px; padding-left: 10px;\" valign=\"top\">\n";
                myString = myString + "															<table class=\"social_table\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-tspace: 0; mso-table-rspace: 0; mso-table-bspace: 0; mso-table-lspace: 0;\" valign=\"top\">\n";
                myString = myString + "																<tbody>\n";
                myString = myString + "																	<tr style=\"vertical-align: top; display: inline-block; text-align: center;\" align=\"center\" valign=\"top\">\n";
                myString = myString + "																		<td style=\"word-break: break-word; vertical-align: top; padding-bottom: 0; padding-right: 10px; padding-left: 10px;\" valign=\"top\"><a href=\"https://www.facebook.com/Smartsense-Yaz%C4%B1l%C4%B1m-106739121451383\" target=\"_blank\"><img width=\"32\" height=\"32\" src=\"https://d2fi4ri5dhpqd1.cloudfront.net/public/resources/social-networks-icon-sets/t-outline-circle-white/facebook@2x.png\" alt=\"Facebook\" title=\"Facebook\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; height: auto; border: 0; display: block;\"></a></td>\n";
                myString = myString + "																		<td style=\"word-break: break-word; vertical-align: top; padding-bottom: 0; padding-right: 10px; padding-left: 10px;\" valign=\"top\"><a href=\"https://twitter.com/SmartsenseMedia\" target=\"_blank\"><img width=\"32\" height=\"32\" src=\"https://d2fi4ri5dhpqd1.cloudfront.net/public/resources/social-networks-icon-sets/t-outline-circle-white/twitter@2x.png\" alt=\"Twitter\" title=\"Twitter\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; height: auto; border: 0; display: block;\"></a></td>\n";
                myString = myString + "																		<td style=\"word-break: break-word; vertical-align: top; padding-bottom: 0; padding-right: 10px; padding-left: 10px;\" valign=\"top\"><a href=\"https://www.instagram.com/smartsense.media/\" target=\"_blank\"><img width=\"32\" height=\"32\" src=\"https://d2fi4ri5dhpqd1.cloudfront.net/public/resources/social-networks-icon-sets/t-outline-circle-white/instagram@2x.png\" alt=\"Instagram\" title=\"Instagram\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; height: auto; border: 0; display: block;\"></a></td>\n";
                myString = myString + "																		<td style=\"word-break: break-word; vertical-align: top; padding-bottom: 0; padding-right: 10px; padding-left: 10px;\" valign=\"top\"><a href=\"https://www.youtube.com/channel/UCa1owNbtRVRuBcO77dO2YFg\" target=\"_blank\"><img width=\"32\" height=\"32\" src=\"https://d2fi4ri5dhpqd1.cloudfront.net/public/resources/social-networks-icon-sets/t-outline-circle-white/youtube@2x.png\" alt=\"YouTube\" title=\"YouTube\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; height: auto; border: 0; display: block;\"></a></td>\n";
                myString = myString + "																	</tr>\n";
                myString = myString + "																</tbody>\n";
                myString = myString + "															</table>\n";
                myString = myString + "														</td>\n";
                myString = myString + "													</tr>\n";
                myString = myString + "												</tbody>\n";
                myString = myString + "											</table>\n";
                myString = myString + "											<table class=\"divider\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" role=\"presentation\" valign=\"top\">\n";
                myString = myString + "												<tbody>\n";
                myString = myString + "													<tr style=\"vertical-align: top;\" valign=\"top\">\n";
                myString = myString + "														<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 10px; padding-right: 40px; padding-bottom: 10px; padding-left: 40px;\" valign=\"top\">\n";
                myString = myString + "															<table class=\"divider_content\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-top: 1px solid #EEEEEE; width: 100%;\" align=\"center\" role=\"presentation\" valign=\"top\">\n";
                myString = myString + "																<tbody>\n";
                myString = myString + "																	<tr style=\"vertical-align: top;\" valign=\"top\">\n";
                myString = myString + "																		<td style=\"word-break: break-word; vertical-align: top; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\"><span></span></td>\n";
                myString = myString + "																	</tr>\n";
                myString = myString + "																</tbody>\n";
                myString = myString + "															</table>\n";
                myString = myString + "														</td>\n";
                myString = myString + "													</tr>\n";
                myString = myString + "												</tbody>\n";
                myString = myString + "											</table>\n";
                myString = myString + "											<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 40px; padding-left: 40px; padding-top: 15px; padding-bottom: 20px; font-family: Tahoma, sans-serif\"><![endif]-->\n";
                myString = myString + "											<div style=\"color:#e2e2e2;font-family:Montserrat, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif;line-height:1.2;padding-top:15px;padding-right:40px;padding-bottom:20px;padding-left:40px;\">\n";
                myString = myString + "												<div class=\"txtTinyMce-wrapper\" style=\"line-height: 1.2; font-size: 12px; font-family: Montserrat, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif; color: #e2e2e2; mso-line-height-alt: 14px;\">\n";
                myString = myString + "													<p style=\"margin: 0; font-size: 14px; line-height: 1.2; word-break: break-word; text-align: center; mso-line-height-alt: 17px; margin-top: 0; margin-bottom: 0;\"><span style=\"color: #ececec;\"><span style=\"font-size: 12px;\">Smartsense Copyright © 2021</span><span style=\"font-size: 12px;\"><a href=\"http://www.example.com\" target=\"_blank\" style=\"text-decoration: underline; color: #ececec;\" rel=\"noopener\"> </a></span></span></p>\n";
                myString = myString + "												</div>\n";
                myString = myString + "											</div>\n";
                myString = myString + "											<!--[if mso]></td></tr></table><![endif]-->\n";
                myString = myString + "											<!--[if (!mso)&(!IE)]><!-->\n";
                myString = myString + "										</div>\n";
                myString = myString + "										<!--<![endif]-->\n";
                myString = myString + "									</div>\n";
                myString = myString + "								</div>\n";
                myString = myString + "								<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n";
                myString = myString + "								<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n";
                myString = myString + "							</div>\n";
                myString = myString + "						</div>\n";
                myString = myString + "					</div>\n";
                myString = myString + "					<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n";
                myString = myString + "				</td>\n";
                myString = myString + "			</tr>\n";
                myString = myString + "		</tbody>\n";
                myString = myString + "	</table>\n";
                myString = myString + "	<!--[if (IE)]></div><![endif]-->\n";
                myString = myString + "</body>\n";
                myString = myString + "\n";
                myString = myString + "</html>\n";

            if (mail.EndsWith("@outlook.com") || mail.EndsWith("@hotmail.com") || mail.EndsWith("@outlook.com.tr"))
            {

                MailMessage msg = new MailMessage();
                SmtpClient client = new SmtpClient();
                client.Credentials = new System.Net.NetworkCredential("insense.medya@outlook.com", "Sagtek21");
                client.Port = 587;
                client.Host = "smtp-mail.outlook.com";
                client.EnableSsl = true;
                msg.To.Add(mail);
                msg.From = new MailAddress("insense.medya@outlook.com", "insense");
                msg.Subject = "Email Doğrulaması";
                msg.Body = myString;
                client.UseDefaultCredentials = false;

                client.Send(msg);
            }
            else {
              verifyUrl = "http://hasta.smartsense.com.tr/VerifyEmail/" + guid;



                MailMessage msg = new MailMessage();
                SmtpClient client = new SmtpClient();
                client.Credentials = new System.Net.NetworkCredential("sagtekyazilim@gmail.com", "Sagtek21");
                client.Port = 587;
                client.Host = "smtp.gmail.com";
                client.EnableSsl = true;
                msg.To.Add(mail);
                msg.From = new MailAddress("sagtekyazilim@gmail.com", "Smartsense Mail Doğrulaması");
                msg.Subject = "Email Doğrulaması";
                msg.IsBodyHtml = true;
                msg.Body = myString;
               
               
                client.UseDefaultCredentials = false;
                client.Send(msg);

            }

           


        }
        public void SendResetEmail(string mail, string guid)
        {

            string verifyUrl = "http://hasta.smartsense.com.tr/ResetPassword/" + guid;
            string myString = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n";
            myString = myString + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n";
            myString = myString + "\n";
            myString = myString + "<head>\n";
            myString = myString + "	<!--[if gte mso 9]><xml><o:OfficeDocumentSettings><o:AllowPNG/><o:PixelsPerInch>96</o:PixelsPerInch></o:OfficeDocumentSettings></xml><![endif]-->\n";
            myString = myString + "	<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n";
            myString = myString + "	<meta name=\"viewport\" content=\"width=device-width\">\n";
            myString = myString + "	<!--[if !mso]><!-->\n";
            myString = myString + "	<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n";
            myString = myString + "	<!--<![endif]-->\n";
            myString = myString + "	<title></title>\n";
            myString = myString + "	<!--[if !mso]><!-->\n";
            myString = myString + "	<link href=\"https://fonts.googleapis.com/css?family=Montserrat\" rel=\"stylesheet\" type=\"text/css\">\n";
            myString = myString + "	<!--<![endif]-->\n";
            myString = myString + "	<style type=\"text/css\">\n";
            myString = myString + "		body {\n";
            myString = myString + "			margin: 0;\n";
            myString = myString + "			padding: 0;\n";
            myString = myString + "		}\n";
            myString = myString + "\n";
            myString = myString + "		table,\n";
            myString = myString + "		td,\n";
            myString = myString + "		tr {\n";
            myString = myString + "			vertical-align: top;\n";
            myString = myString + "			border-collapse: collapse;\n";
            myString = myString + "		}\n";
            myString = myString + "\n";
            myString = myString + "		* {\n";
            myString = myString + "			line-height: inherit;\n";
            myString = myString + "		}\n";
            myString = myString + "\n";
            myString = myString + "		a[x-apple-data-detectors=true] {\n";
            myString = myString + "			color: inherit !important;\n";
            myString = myString + "			text-decoration: none !important;\n";
            myString = myString + "		}\n";
            myString = myString + "	</style>\n";
            myString = myString + "	<style type=\"text/css\" id=\"media-query\">\n";
            myString = myString + "		@media (max-width: 660px) {\n";
            myString = myString + "\n";
            myString = myString + "			.block-grid,\n";
            myString = myString + "			.col {\n";
            myString = myString + "				min-width: 320px !important;\n";
            myString = myString + "				max-width: 100% !important;\n";
            myString = myString + "				display: block !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.block-grid {\n";
            myString = myString + "				width: 100% !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.col {\n";
            myString = myString + "				width: 100% !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.col_cont {\n";
            myString = myString + "				margin: 0 auto;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			img.fullwidth,\n";
            myString = myString + "			img.fullwidthOnMobile {\n";
            myString = myString + "				width: 100% !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.no-stack .col {\n";
            myString = myString + "				min-width: 0 !important;\n";
            myString = myString + "				display: table-cell !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.no-stack.two-up .col {\n";
            myString = myString + "				width: 50% !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.no-stack .col.num2 {\n";
            myString = myString + "				width: 16.6% !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.no-stack .col.num3 {\n";
            myString = myString + "				width: 25% !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.no-stack .col.num4 {\n";
            myString = myString + "				width: 33% !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.no-stack .col.num5 {\n";
            myString = myString + "				width: 41.6% !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.no-stack .col.num6 {\n";
            myString = myString + "				width: 50% !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.no-stack .col.num7 {\n";
            myString = myString + "				width: 58.3% !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.no-stack .col.num8 {\n";
            myString = myString + "				width: 66.6% !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.no-stack .col.num9 {\n";
            myString = myString + "				width: 75% !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.no-stack .col.num10 {\n";
            myString = myString + "				width: 83.3% !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.video-block {\n";
            myString = myString + "				max-width: none !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.mobile_hide {\n";
            myString = myString + "				min-height: 0px;\n";
            myString = myString + "				max-height: 0px;\n";
            myString = myString + "				max-width: 0px;\n";
            myString = myString + "				display: none;\n";
            myString = myString + "				overflow: hidden;\n";
            myString = myString + "				font-size: 0px;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.desktop_hide {\n";
            myString = myString + "				display: block !important;\n";
            myString = myString + "				max-height: none !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.img-container.big img {\n";
            myString = myString + "				width: auto !important;\n";
            myString = myString + "			}\n";
            myString = myString + "		}\n";
            myString = myString + "	</style>\n";
            myString = myString + "</head>\n";
            myString = myString + "\n";
            myString = myString + "<body class=\"clean-body\" style=\"margin: 0; padding: 0; -webkit-text-size-adjust: 100%; background-color: #ececec;\">\n";
            myString = myString + "	<!--[if IE]><div class=\"ie-browser\"><![endif]-->\n";
            myString = myString + "	<table class=\"nl-container\" style=\"table-layout: fixed; vertical-align: top; min-width: 320px; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ececec; width: 100%;\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" width=\"100%\" bgcolor=\"#ececec\" valign=\"top\">\n";
            myString = myString + "		<tbody>\n";
            myString = myString + "			<tr style=\"vertical-align: top;\" valign=\"top\">\n";
            myString = myString + "				<td style=\"word-break: break-word; vertical-align: top;\" valign=\"top\">\n";
            myString = myString + "					<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" style=\"background-color:#ececec\"><![endif]-->\n";
            myString = myString + "					<div style=\"background-color:transparent;\">\n";
            myString = myString + "						<div class=\"block-grid \" style=\"min-width: 320px; max-width: 640px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; Margin: 0 auto; background-color: #00695c;\">\n";
            myString = myString + "							<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:#00695c;\">\n";
            myString = myString + "								<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background-color:transparent;\"><tr><td align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:640px\"><tr class=\"layout-full-width\" style=\"background-color:#00695c\"><![endif]-->\n";
            myString = myString + "								<!--[if (mso)|(IE)]><td align=\"center\" width=\"640\" style=\"background-color:#00695c;width:640px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top:0px; padding-bottom:0px;\"><![endif]-->\n";
            myString = myString + "								<div class=\"col num12\" style=\"min-width: 320px; max-width: 640px; display: table-cell; vertical-align: top; width: 640px;\">\n";
            myString = myString + "									<div class=\"col_cont\" style=\"width:100% !important;\">\n";
            myString = myString + "										<!--[if (!mso)&(!IE)]><!-->\n";
            myString = myString + "										<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n";
            myString = myString + "											<!--<![endif]-->\n";
            myString = myString + "											<div class=\"img-container center fixedwidth\" align=\"center\" style=\"padding-right: 0px;padding-left: 0px;\">\n";
            myString = myString + "												<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr style=\"line-height:0px\"><td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\"><![endif]-->\n";
            myString = myString + "												<div style=\"font-size:1px;line-height:15px\">&nbsp;</div><img class=\"center fixedwidth\" align=\"center\" border=\"0\" src=\"https://www.insense.com.tr/lock2.png\" alt=\"Image of lock &amp; key.\" title=\"Image of lock &amp; key.\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; height: auto; border: 0; width: 256px; max-width: 100%; display: block;\" width=\"256\">\n";
            myString = myString + "												<div style=\"font-size:1px;line-height:15px\">&nbsp;</div>\n";
            myString = myString + "												<!--[if mso]></td></tr></table><![endif]-->\n";
            myString = myString + "											</div>\n";
            myString = myString + "											<!--[if (!mso)&(!IE)]><!-->\n";
            myString = myString + "										</div>\n";
            myString = myString + "										<!--<![endif]-->\n";
            myString = myString + "									</div>\n";
            myString = myString + "								</div>\n";
            myString = myString + "								<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n";
            myString = myString + "								<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n";
            myString = myString + "							</div>\n";
            myString = myString + "						</div>\n";
            myString = myString + "					</div>\n";
            myString = myString + "					<div style=\"background-color:transparent;\">\n";
            myString = myString + "						<div class=\"block-grid \" style=\"min-width: 320px; max-width: 640px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; Margin: 0 auto; background-color: transparent;\">\n";
            myString = myString + "							<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n";
            myString = myString + "								<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background-color:transparent;\"><tr><td align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:640px\"><tr class=\"layout-full-width\" style=\"background-color:transparent\"><![endif]-->\n";
            myString = myString + "								<!--[if (mso)|(IE)]><td align=\"center\" width=\"640\" style=\"background-color:transparent;width:640px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top:0px; padding-bottom:0px;\"><![endif]-->\n";
            myString = myString + "								<div class=\"col num12\" style=\"min-width: 320px; max-width: 640px; display: table-cell; vertical-align: top; width: 640px;\">\n";
            myString = myString + "									<div class=\"col_cont\" style=\"width:100% !important;\">\n";
            myString = myString + "										<!--[if (!mso)&(!IE)]><!-->\n";
            myString = myString + "										<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n";
            myString = myString + "											<!--<![endif]-->\n";
            myString = myString + "											<table class=\"divider\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" role=\"presentation\" valign=\"top\">\n";
            myString = myString + "												<tbody>\n";
            myString = myString + "													<tr style=\"vertical-align: top;\" valign=\"top\">\n";
            myString = myString + "														<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 30px; padding-right: 0px; padding-bottom: 0px; padding-left: 0px;\" valign=\"top\">\n";
            myString = myString + "															<table class=\"divider_content\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-top: 0px solid #BBBBBB; width: 100%;\" align=\"center\" role=\"presentation\" valign=\"top\">\n";
            myString = myString + "																<tbody>\n";
            myString = myString + "																	<tr style=\"vertical-align: top;\" valign=\"top\">\n";
            myString = myString + "																		<td style=\"word-break: break-word; vertical-align: top; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\"><span></span></td>\n";
            myString = myString + "																	</tr>\n";
            myString = myString + "																</tbody>\n";
            myString = myString + "															</table>\n";
            myString = myString + "														</td>\n";
            myString = myString + "													</tr>\n";
            myString = myString + "												</tbody>\n";
            myString = myString + "											</table>\n";
            myString = myString + "											<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 40px; padding-left: 40px; padding-top: 10px; padding-bottom: 10px; font-family: 'Trebuchet MS', Tahoma, sans-serif\"><![endif]-->\n";
            myString = myString + "											<div style=\"color:#555555;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:1.2;padding-top:10px;padding-right:40px;padding-bottom:10px;padding-left:40px;\">\n";
            myString = myString + "												<div class=\"txtTinyMce-wrapper\" style=\"line-height: 1.2; font-size: 12px; font-family: 'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 14px;\">\n";
            myString = myString + "													<p style=\"margin: 0; font-size: 16px; line-height: 1.2; text-align: center; word-break: break-word; mso-line-height-alt: 19px; margin-top: 0; margin-bottom: 0;\"><strong><span style=\"font-size: 30px;\"><span style=\"color: #555555;\">&#350;ifrenizi mi</span> unuttunuz?</span></strong></p>\n";
            myString = myString + "												</div>\n";
            myString = myString + "											</div>\n";
            myString = myString + "											<!--[if mso]></td></tr></table><![endif]-->\n";
            myString = myString + "											<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 10px; padding-left: 10px; padding-top: 10px; padding-bottom: 10px; font-family: 'Trebuchet MS', Tahoma, sans-serif\"><![endif]-->\n";
            myString = myString + "											<div style=\"color:#ffffff;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:1.5;padding-top:10px;padding-right:10px;padding-bottom:10px;padding-left:10px;\">\n";
            myString = myString + "												<div class=\"txtTinyMce-wrapper\" style=\"line-height: 1.5; font-size: 12px; font-family: 'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #ffffff; mso-line-height-alt: 18px;\">\n";
            myString = myString + "													<p style=\"margin: 0; font-size: 15px; line-height: 1.5; text-align: center; word-break: break-word; mso-line-height-alt: 23px; margin-top: 0; margin-bottom: 0;\"><span style=\"color: #555555; font-size: 15px;\">&#350;ifrenizi alttaki butona t&#305;klayarak s&#305;f&#305;rlayabilirsiniz.</span></p>\n";
            myString = myString + "												</div>\n";
            myString = myString + "											</div>\n";
            myString = myString + "											<!--[if mso]></td></tr></table><![endif]-->\n";
            myString = myString + "											<div class=\"button-container\" align=\"center\" style=\"padding-top:20px;padding-right:10px;padding-bottom:0px;padding-left:10px;\">\n";
            myString = myString + "												<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-spacing: 0; border-collapse: collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;\"><tr><td style=\"padding-top: 20px; padding-right: 10px; padding-bottom: 0px; padding-left: 10px\" align=\"center\"><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\"  href=\"" + verifyUrl + "\" style=\"height:46.5pt;width:129.75pt;v-text-anchor:middle;\" arcsize=\"57%\" stroke=\"false\" fillcolor=\"#00695c\"><w:anchorlock/><v:textbox inset=\"0,0,0,0\"><center style=\"color:#ffffff; font-family:'Trebuchet MS', Tahoma, sans-serif; font-size:16px\"><![endif]--><a href=\"" + verifyUrl+" \" target=\"_blank\" style=\"-webkit-text-size-adjust: none; text-decoration: none; display: inline-block; color: #ffffff; background-color: #00695c; border-radius: 35px; -webkit-border-radius: 35px; -moz-border-radius: 35px; width: auto; width: auto; border-top: 1px solid #00695c; border-right: 1px solid #00695c; border-bottom: 1px solid #00695c; border-left: 1px solid #00695c; padding-top: 15px; padding-bottom: 15px; font-family: 'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; text-align: center; mso-border-alt: none; word-break: keep-all;\"><span style=\"padding-left:30px;padding-right:30px;font-size:16px;display:inline-block;letter-spacing:undefined;\"><span style=\"font-size: 16px; margin: 0; line-height: 2; word-break: break-word; mso-line-height-alt: 32px;\"><span style=\"font-size: 16px; line-height: 32px;\" data-mce-style=\"font-size: 16px; line-height: 32px;\"><strong>&#350;&#304;FRE SIFIRLA</strong></span></span></span></a>\n";
            myString = myString + "												<!--[if mso]></center></v:textbox></v:roundrect></td></tr></table><![endif]-->\n";
            myString = myString + "											</div>\n";
            myString = myString + "											<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 10px; padding-left: 10px; padding-top: 40px; padding-bottom: 40px; font-family: Tahoma, sans-serif\"><![endif]-->\n";
            myString = myString + "											<div style=\"color:#e5e5e5;font-family:Montserrat, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif;line-height:1.2;padding-top:40px;padding-right:10px;padding-bottom:40px;padding-left:10px;\">\n";
            myString = myString + "												<div class=\"txtTinyMce-wrapper\" style=\"font-size: 14px; line-height: 1.2; color: #e5e5e5; font-family: Montserrat, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif; mso-line-height-alt: 17px;\">\n";
            myString = myString + "													<p style=\"margin: 0; text-align: center; line-height: 1.2; word-break: break-word; font-size: 13px; mso-line-height-alt: 16px; mso-ansi-font-size: 14px; margin-top: 0; margin-bottom: 0;\"><span style=\"font-size: 13px; color: #555555; mso-ansi-font-size: 14px;\">E&#287;er &#351;ifre s&#305;f&#305;rlama iste&#287;inde bulunmad&#305;ysan&#305;z bu e-maili göz ard&#305; edebilir ya da silebilirsiniz.</span></p>\n";
            myString = myString + "												</div>\n";
            myString = myString + "											</div>\n";
            myString = myString + "											<!--[if mso]></td></tr></table><![endif]-->\n";
            myString = myString + "											<!--[if (!mso)&(!IE)]><!-->\n";
            myString = myString + "										</div>\n";
            myString = myString + "										<!--<![endif]-->\n";
            myString = myString + "									</div>\n";
            myString = myString + "								</div>\n";
            myString = myString + "								<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n";
            myString = myString + "								<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n";
            myString = myString + "							</div>\n";
            myString = myString + "						</div>\n";
            myString = myString + "					</div>\n";
            myString = myString + "					<div style=\"background-color:transparent;\">\n";
            myString = myString + "						<div class=\"block-grid \" style=\"min-width: 320px; max-width: 640px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; Margin: 0 auto; background-color: transparent;\">\n";
            myString = myString + "							<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n";
            myString = myString + "								<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background-color:transparent;\"><tr><td align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:640px\"><tr class=\"layout-full-width\" style=\"background-color:transparent\"><![endif]-->\n";
            myString = myString + "								<!--[if (mso)|(IE)]><td align=\"center\" width=\"640\" style=\"background-color:transparent;width:640px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top:0px; padding-bottom:0px;\"><![endif]-->\n";
            myString = myString + "								<div class=\"col num12\" style=\"min-width: 320px; max-width: 640px; display: table-cell; vertical-align: top; width: 640px;\">\n";
            myString = myString + "									<div class=\"col_cont\" style=\"width:100% !important;\">\n";
            myString = myString + "										<!--[if (!mso)&(!IE)]><!-->\n";
            myString = myString + "										<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n";
            myString = myString + "											<!--<![endif]-->\n";
            myString = myString + "											<div class=\"img-container center fixedwidth\" align=\"center\" style=\"padding-right: 0px;padding-left: 0px;\">\n";
            myString = myString + "												<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr style=\"line-height:0px\"><td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\"><![endif]--><a href=\"https://www.smartsense.com.tr\" target=\"_blank\" style=\"outline:none\" tabindex=\"-1\"><img class=\"center fixedwidth\" align=\"center\" border=\"0\" src=\"https://www.smartsense.com.tr/img/ss-logo.PNG\" alt=\"Smartsense Logo\" title=\"Smartsense Logo\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; height: auto; border: 0; width: 160px; max-width: 100%; display: block;\" width=\"160\"></a>\n";
            myString = myString + "												<!--[if mso]></td></tr></table><![endif]-->\n";
            myString = myString + "											</div>\n";
            myString = myString + "											<!--[if (!mso)&(!IE)]><!-->\n";
            myString = myString + "										</div>\n";
            myString = myString + "										<!--<![endif]-->\n";
            myString = myString + "									</div>\n";
            myString = myString + "								</div>\n";
            myString = myString + "								<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n";
            myString = myString + "								<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n";
            myString = myString + "							</div>\n";
            myString = myString + "						</div>\n";
            myString = myString + "					</div>\n";
            myString = myString + "					<div style=\"background-color:transparent;\">\n";
            myString = myString + "						<div class=\"block-grid \" style=\"min-width: 320px; max-width: 640px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; Margin: 0 auto; background-color: #00695c;\">\n";
            myString = myString + "							<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:#00695c;\">\n";
            myString = myString + "								<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background-color:transparent;\"><tr><td align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:640px\"><tr class=\"layout-full-width\" style=\"background-color:#00695c\"><![endif]-->\n";
            myString = myString + "								<!--[if (mso)|(IE)]><td align=\"center\" width=\"640\" style=\"background-color:#00695c;width:640px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top:0px; padding-bottom:0px;\"><![endif]-->\n";
            myString = myString + "								<div class=\"col num12\" style=\"min-width: 320px; max-width: 640px; display: table-cell; vertical-align: top; width: 640px;\">\n";
            myString = myString + "									<div class=\"col_cont\" style=\"width:100% !important;\">\n";
            myString = myString + "										<!--[if (!mso)&(!IE)]><!-->\n";
            myString = myString + "										<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n";
            myString = myString + "											<!--<![endif]-->\n";
            myString = myString + "											<table class=\"social_icons\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" valign=\"top\">\n";
            myString = myString + "												<tbody>\n";
            myString = myString + "													<tr style=\"vertical-align: top;\" valign=\"top\">\n";
            myString = myString + "														<td style=\"word-break: break-word; vertical-align: top; padding-top: 20px; padding-right: 10px; padding-bottom: 10px; padding-left: 10px;\" valign=\"top\">\n";
            myString = myString + "															<table class=\"social_table\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-tspace: 0; mso-table-rspace: 0; mso-table-bspace: 0; mso-table-lspace: 0;\" valign=\"top\">\n";
            myString = myString + "																<tbody>\n";
            myString = myString + "																	<tr style=\"vertical-align: top; display: inline-block; text-align: center;\" align=\"center\" valign=\"top\">\n";
            myString = myString + "																		<td style=\"word-break: break-word; vertical-align: top; padding-bottom: 0; padding-right: 10px; padding-left: 10px;\" valign=\"top\"><a href=\"https://www.facebook.com/Smartsense-Yaz%C4%B1l%C4%B1m-106739121451383\" target=\"_blank\"><img width=\"32\" height=\"32\" src=\"https://d2fi4ri5dhpqd1.cloudfront.net/public/resources/social-networks-icon-sets/t-outline-circle-white/facebook@2x.png\" alt=\"Facebook\" title=\"Facebook\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; height: auto; border: 0; display: block;\"></a></td>\n";
            myString = myString + "																		<td style=\"word-break: break-word; vertical-align: top; padding-bottom: 0; padding-right: 10px; padding-left: 10px;\" valign=\"top\"><a href=\"https://twitter.com/SmartsenseMedia\" target=\"_blank\"><img width=\"32\" height=\"32\" src=\"https://d2fi4ri5dhpqd1.cloudfront.net/public/resources/social-networks-icon-sets/t-outline-circle-white/twitter@2x.png\" alt=\"Twitter\" title=\"Twitter\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; height: auto; border: 0; display: block;\"></a></td>\n";
            myString = myString + "																		<td style=\"word-break: break-word; vertical-align: top; padding-bottom: 0; padding-right: 10px; padding-left: 10px;\" valign=\"top\"><a href=\"https://www.instagram.com/smartsense.media/\" target=\"_blank\"><img width=\"32\" height=\"32\" src=\"https://d2fi4ri5dhpqd1.cloudfront.net/public/resources/social-networks-icon-sets/t-outline-circle-white/instagram@2x.png\" alt=\"Instagram\" title=\"Instagram\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; height: auto; border: 0; display: block;\"></a></td>\n";
            myString = myString + "																		<td style=\"word-break: break-word; vertical-align: top; padding-bottom: 0; padding-right: 10px; padding-left: 10px;\" valign=\"top\"><a href=\"https://www.youtube.com/channel/UCa1owNbtRVRuBcO77dO2YFg\" target=\"_blank\"><img width=\"32\" height=\"32\" src=\"https://d2fi4ri5dhpqd1.cloudfront.net/public/resources/social-networks-icon-sets/t-outline-circle-white/youtube@2x.png\" alt=\"YouTube\" title=\"YouTube\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; height: auto; border: 0; display: block;\"></a></td>\n";
            myString = myString + "																	</tr>\n";
            myString = myString + "																</tbody>\n";
            myString = myString + "															</table>\n";
            myString = myString + "														</td>\n";
            myString = myString + "													</tr>\n";
            myString = myString + "												</tbody>\n";
            myString = myString + "											</table>\n";
            myString = myString + "											<table class=\"divider\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" role=\"presentation\" valign=\"top\">\n";
            myString = myString + "												<tbody>\n";
            myString = myString + "													<tr style=\"vertical-align: top;\" valign=\"top\">\n";
            myString = myString + "														<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 10px; padding-right: 40px; padding-bottom: 10px; padding-left: 40px;\" valign=\"top\">\n";
            myString = myString + "															<table class=\"divider_content\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-top: 1px solid #EEEEEE; width: 100%;\" align=\"center\" role=\"presentation\" valign=\"top\">\n";
            myString = myString + "																<tbody>\n";
            myString = myString + "																	<tr style=\"vertical-align: top;\" valign=\"top\">\n";
            myString = myString + "																		<td style=\"word-break: break-word; vertical-align: top; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\"><span></span></td>\n";
            myString = myString + "																	</tr>\n";
            myString = myString + "																</tbody>\n";
            myString = myString + "															</table>\n";
            myString = myString + "														</td>\n";
            myString = myString + "													</tr>\n";
            myString = myString + "												</tbody>\n";
            myString = myString + "											</table>\n";
            myString = myString + "											<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 40px; padding-left: 40px; padding-top: 15px; padding-bottom: 20px; font-family: Tahoma, sans-serif\"><![endif]-->\n";
            myString = myString + "											<div style=\"color:#e2e2e2;font-family:Montserrat, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif;line-height:1.2;padding-top:15px;padding-right:40px;padding-bottom:20px;padding-left:40px;\">\n";
            myString = myString + "												<div class=\"txtTinyMce-wrapper\" style=\"line-height: 1.2; font-size: 12px; font-family: Montserrat, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif; color: #e2e2e2; mso-line-height-alt: 14px;\">\n";
            myString = myString + "													<p style=\"margin: 0; font-size: 14px; line-height: 1.2; word-break: break-word; text-align: center; mso-line-height-alt: 17px; margin-top: 0; margin-bottom: 0;\"><span style=\"color: #ececec;\"><span style=\"font-size: 12px;\">Smartsense Copyright © 2021</span><span style=\"font-size: 12px;\"><a href=\"http://www.example.com\" target=\"_blank\" style=\"text-decoration: underline; color: #ececec;\" rel=\"noopener\"> </a></span></span></p>\n";
            myString = myString + "												</div>\n";
            myString = myString + "											</div>\n";
            myString = myString + "											<!--[if mso]></td></tr></table><![endif]-->\n";
            myString = myString + "											<!--[if (!mso)&(!IE)]><!-->\n";
            myString = myString + "										</div>\n";
            myString = myString + "										<!--<![endif]-->\n";
            myString = myString + "									</div>\n";
            myString = myString + "								</div>\n";
            myString = myString + "								<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n";
            myString = myString + "								<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n";
            myString = myString + "							</div>\n";
            myString = myString + "						</div>\n";
            myString = myString + "					</div>\n";
            myString = myString + "					<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n";
            myString = myString + "				</td>\n";
            myString = myString + "			</tr>\n";
            myString = myString + "		</tbody>\n";
            myString = myString + "	</table>\n";
            myString = myString + "	<!--[if (IE)]></div><![endif]-->\n";
            myString = myString + "</body>\n";
            myString = myString + "\n";
            myString = myString + "</html>\n";


            if (mail.EndsWith("@outlook.com") || mail.EndsWith("@hotmail.com") || mail.EndsWith("@outlook.com.tr"))
            {

                MailMessage msg = new MailMessage();
                SmtpClient client = new SmtpClient();
                client.Credentials = new System.Net.NetworkCredential("insense.medya@outlook.com", "Sagtek21");
                client.Port = 587;
                client.Host = "smtp-mail.outlook.com";
                client.EnableSsl = true;
                msg.To.Add(mail);
                msg.From = new MailAddress("insense.medya@outlook.com", "Smartsense Şifre Sıfırlama");
                msg.Subject = "Şifre Sıfırlama";
               
                msg.IsBodyHtml = true;
                msg.Body = myString;

                client.Send(msg);
            }
            else
            {
                MailMessage msg = new MailMessage();
                SmtpClient client = new SmtpClient();
                client.Credentials = new System.Net.NetworkCredential("sagtekyazilim@gmail.com", "Sagtek21");
                client.Port = 587;
                client.Host = "smtp.gmail.com";
                client.EnableSsl = true;
                msg.To.Add(mail);
                msg.From = new MailAddress("sagtekyazilim@gmail.com", "Smartsense Şifre Sıfırlama");
                msg.Subject = "Şifre Sıfırlama";
                msg.IsBodyHtml = true;
                msg.Body = myString;

                client.Send(msg);
            }

        }
        public void ChangedPasswordEmail(string mail)
        {

          //  string verifyUrl = "Mail Adresinize Ait Şifre Değiştirilmiştir \n Şifre Değiştirilme İşlemi Size Ait Değilse Lütfen Destek İle İletişime Geçin." + "\n Smartsense Biyomedikal Yazılım \n www.smartsense.com.tr ";
            string myString = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n";
            myString = myString + "\n";
            myString = myString + "<head>\n";
            myString = myString + "	<!--[if gte mso 9]><xml><o:OfficeDocumentSettings><o:AllowPNG/><o:PixelsPerInch>96</o:PixelsPerInch></o:OfficeDocumentSettings></xml><![endif]-->\n";
            myString = myString + "	<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n";
            myString = myString + "	<meta name=\"viewport\" content=\"width=device-width\">\n";
            myString = myString + "	<!--[if !mso]><!-->\n";
            myString = myString + "	<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n";
            myString = myString + "	<!--<![endif]-->\n";
            myString = myString + "	<title></title>\n";
            myString = myString + "	<!--[if !mso]><!-->\n";
            myString = myString + "	<link href=\"https://fonts.googleapis.com/css?family=Montserrat\" rel=\"stylesheet\" type=\"text/css\">\n";
            myString = myString + "	<!--<![endif]-->\n";
            myString = myString + "	<style type=\"text/css\">\n";
            myString = myString + "		body {\n";
            myString = myString + "			margin: 0;\n";
            myString = myString + "			padding: 0;\n";
            myString = myString + "		}\n";
            myString = myString + "\n";
            myString = myString + "		table,\n";
            myString = myString + "		td,\n";
            myString = myString + "		tr {\n";
            myString = myString + "			vertical-align: top;\n";
            myString = myString + "			border-collapse: collapse;\n";
            myString = myString + "		}\n";
            myString = myString + "\n";
            myString = myString + "		* {\n";
            myString = myString + "			line-height: inherit;\n";
            myString = myString + "		}\n";
            myString = myString + "\n";
            myString = myString + "		a[x-apple-data-detectors=true] {\n";
            myString = myString + "			color: inherit !important;\n";
            myString = myString + "			text-decoration: none !important;\n";
            myString = myString + "		}\n";
            myString = myString + "	</style>\n";
            myString = myString + "	<style type=\"text/css\" id=\"media-query\">\n";
            myString = myString + "		@media (max-width: 660px) {\n";
            myString = myString + "\n";
            myString = myString + "			.block-grid,\n";
            myString = myString + "			.col {\n";
            myString = myString + "				min-width: 320px !important;\n";
            myString = myString + "				max-width: 100% !important;\n";
            myString = myString + "				display: block !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.block-grid {\n";
            myString = myString + "				width: 100% !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.col {\n";
            myString = myString + "				width: 100% !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.col_cont {\n";
            myString = myString + "				margin: 0 auto;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			img.fullwidth,\n";
            myString = myString + "			img.fullwidthOnMobile {\n";
            myString = myString + "				width: 100% !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.no-stack .col {\n";
            myString = myString + "				min-width: 0 !important;\n";
            myString = myString + "				display: table-cell !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.no-stack.two-up .col {\n";
            myString = myString + "				width: 50% !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.no-stack .col.num2 {\n";
            myString = myString + "				width: 16.6% !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.no-stack .col.num3 {\n";
            myString = myString + "				width: 25% !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.no-stack .col.num4 {\n";
            myString = myString + "				width: 33% !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.no-stack .col.num5 {\n";
            myString = myString + "				width: 41.6% !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.no-stack .col.num6 {\n";
            myString = myString + "				width: 50% !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.no-stack .col.num7 {\n";
            myString = myString + "				width: 58.3% !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.no-stack .col.num8 {\n";
            myString = myString + "				width: 66.6% !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.no-stack .col.num9 {\n";
            myString = myString + "				width: 75% !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.no-stack .col.num10 {\n";
            myString = myString + "				width: 83.3% !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.video-block {\n";
            myString = myString + "				max-width: none !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.mobile_hide {\n";
            myString = myString + "				min-height: 0px;\n";
            myString = myString + "				max-height: 0px;\n";
            myString = myString + "				max-width: 0px;\n";
            myString = myString + "				display: none;\n";
            myString = myString + "				overflow: hidden;\n";
            myString = myString + "				font-size: 0px;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.desktop_hide {\n";
            myString = myString + "				display: block !important;\n";
            myString = myString + "				max-height: none !important;\n";
            myString = myString + "			}\n";
            myString = myString + "\n";
            myString = myString + "			.img-container.big img {\n";
            myString = myString + "				width: auto !important;\n";
            myString = myString + "			}\n";
            myString = myString + "		}\n";
            myString = myString + "	</style>\n";
            myString = myString + "</head>\n";
            myString = myString + "\n";
            myString = myString + "<body class=\"clean-body\" style=\"margin: 0; padding: 0; -webkit-text-size-adjust: 100%; background-color: #ececec;\">\n";
            myString = myString + "	<!--[if IE]><div class=\"ie-browser\"><![endif]-->\n";
            myString = myString + "	<table class=\"nl-container\" style=\"table-layout: fixed; vertical-align: top; min-width: 320px; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ececec; width: 100%;\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" width=\"100%\" bgcolor=\"#ececec\" valign=\"top\">\n";
            myString = myString + "		<tbody>\n";
            myString = myString + "			<tr style=\"vertical-align: top;\" valign=\"top\">\n";
            myString = myString + "				<td style=\"word-break: break-word; vertical-align: top;\" valign=\"top\">\n";
            myString = myString + "					<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" style=\"background-color:#ececec\"><![endif]-->\n";
            myString = myString + "					<div style=\"background-color:transparent;\">\n";
            myString = myString + "						<div class=\"block-grid \" style=\"min-width: 320px; max-width: 640px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; Margin: 0 auto; background-color: #00695c;\">\n";
            myString = myString + "							<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:#00695c;\">\n";
            myString = myString + "								<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background-color:transparent;\"><tr><td align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:640px\"><tr class=\"layout-full-width\" style=\"background-color:#00695c\"><![endif]-->\n";
            myString = myString + "								<!--[if (mso)|(IE)]><td align=\"center\" width=\"640\" style=\"background-color:#00695c;width:640px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top:0px; padding-bottom:0px;\"><![endif]-->\n";
            myString = myString + "								<div class=\"col num12\" style=\"min-width: 320px; max-width: 640px; display: table-cell; vertical-align: top; width: 640px;\">\n";
            myString = myString + "									<div class=\"col_cont\" style=\"width:100% !important;\">\n";
            myString = myString + "										<!--[if (!mso)&(!IE)]><!-->\n";
            myString = myString + "										<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n";
            myString = myString + "											<!--<![endif]-->\n";
            myString = myString + "											<div class=\"img-container center fixedwidth\" align=\"center\" style=\"padding-right: 0px;padding-left: 0px;\">\n";
            myString = myString + "												<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr style=\"line-height:0px\"><td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\"><![endif]-->\n";
            myString = myString + "												<div style=\"font-size:1px;line-height:15px\">&nbsp;</div><img class=\"center fixedwidth\" align=\"center\" border=\"0\" src=\"https://www.insense.com.tr/lock2.png\" alt=\"Image of lock &amp; key.\" title=\"Image of lock &amp; key.\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; height: auto; border: 0; width: 256px; max-width: 100%; display: block;\" width=\"256\">\n";
            myString = myString + "												<div style=\"font-size:1px;line-height:15px\">&nbsp;</div>\n";
            myString = myString + "												<!--[if mso]></td></tr></table><![endif]-->\n";
            myString = myString + "											</div>\n";
            myString = myString + "											<!--[if (!mso)&(!IE)]><!-->\n";
            myString = myString + "										</div>\n";
            myString = myString + "										<!--<![endif]-->\n";
            myString = myString + "									</div>\n";
            myString = myString + "								</div>\n";
            myString = myString + "								<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n";
            myString = myString + "								<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n";
            myString = myString + "							</div>\n";
            myString = myString + "						</div>\n";
            myString = myString + "					</div>\n";
            myString = myString + "					<div style=\"background-color:transparent;\">\n";
            myString = myString + "						<div class=\"block-grid \" style=\"min-width: 320px; max-width: 640px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; Margin: 0 auto; background-color: transparent;\">\n";
            myString = myString + "							<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n";
            myString = myString + "								<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background-color:transparent;\"><tr><td align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:640px\"><tr class=\"layout-full-width\" style=\"background-color:transparent\"><![endif]-->\n";
            myString = myString + "								<!--[if (mso)|(IE)]><td align=\"center\" width=\"640\" style=\"background-color:transparent;width:640px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top:0px; padding-bottom:0px;\"><![endif]-->\n";
            myString = myString + "								<div class=\"col num12\" style=\"min-width: 320px; max-width: 640px; display: table-cell; vertical-align: top; width: 640px;\">\n";
            myString = myString + "									<div class=\"col_cont\" style=\"width:100% !important;\">\n";
            myString = myString + "										<!--[if (!mso)&(!IE)]><!-->\n";
            myString = myString + "										<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n";
            myString = myString + "											<!--<![endif]-->\n";
            myString = myString + "											<table class=\"divider\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" role=\"presentation\" valign=\"top\">\n";
            myString = myString + "												<tbody>\n";
            myString = myString + "													<tr style=\"vertical-align: top;\" valign=\"top\">\n";
            myString = myString + "														<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 30px; padding-right: 0px; padding-bottom: 0px; padding-left: 0px;\" valign=\"top\">\n";
            myString = myString + "															<table class=\"divider_content\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-top: 0px solid #BBBBBB; width: 100%;\" align=\"center\" role=\"presentation\" valign=\"top\">\n";
            myString = myString + "																<tbody>\n";
            myString = myString + "																	<tr style=\"vertical-align: top;\" valign=\"top\">\n";
            myString = myString + "																		<td style=\"word-break: break-word; vertical-align: top; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\"><span></span></td>\n";
            myString = myString + "																	</tr>\n";
            myString = myString + "																</tbody>\n";
            myString = myString + "															</table>\n";
            myString = myString + "														</td>\n";
            myString = myString + "													</tr>\n";
            myString = myString + "												</tbody>\n";
            myString = myString + "											</table>\n";
            myString = myString + "											<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 40px; padding-left: 40px; padding-top: 10px; padding-bottom: 10px; font-family: 'Trebuchet MS', Tahoma, sans-serif\"><![endif]-->\n";
            myString = myString + "											<div style=\"color:#555555;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:1.2;padding-top:10px;padding-right:40px;padding-bottom:10px;padding-left:40px;\">\n";
            myString = myString + "												<div class=\"txtTinyMce-wrapper\" style=\"line-height: 1.2; font-size: 12px; font-family: 'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 14px;\">\n";
            myString = myString + "													<p style=\"margin: 0; font-size: 30px; line-height: 1.2; text-align: center; word-break: break-word; mso-line-height-alt: 36px; margin-top: 0; margin-bottom: 0;\"><span style=\"font-size: 30px; color: #555555;\"><strong>&#350;ifreniz De&#287;i&#351;tirildi</strong></span></p>\n";
            myString = myString + "												</div>\n";
            myString = myString + "											</div>\n";
            myString = myString + "											<!--[if mso]></td></tr></table><![endif]-->\n";
            myString = myString + "											<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 10px; padding-left: 10px; padding-top: 10px; padding-bottom: 10px; font-family: 'Trebuchet MS', Tahoma, sans-serif\"><![endif]-->\n";
            myString = myString + "											<div style=\"color:#ffffff;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:1.5;padding-top:10px;padding-right:10px;padding-bottom:10px;padding-left:10px;\">\n";
            myString = myString + "												<div class=\"txtTinyMce-wrapper\" style=\"line-height: 1.5; font-size: 12px; font-family: 'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #ffffff; mso-line-height-alt: 18px;\">\n";
            myString = myString + "													<p style=\"margin: 0; font-size: 15px; line-height: 1.5; text-align: center; word-break: break-word; mso-line-height-alt: 23px; margin-top: 0; margin-bottom: 0;\"><span style=\"color: #555555; font-size: 15px;\">&#350;ifre de&#287;i&#351;tirilme i&#351;lemi size ait de&#287;ilse lütfen destek ile ileti&#351;ime geçiniz.</span></p>\n";
            myString = myString + "												</div>\n";
            myString = myString + "											</div>\n";
            myString = myString + "											<!--[if mso]></td></tr></table><![endif]-->\n";
            myString = myString + "											<div class=\"button-container\" align=\"center\" style=\"padding-top:20px;padding-right:10px;padding-bottom:20px;padding-left:10px;\">\n";
            myString = myString + "												<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-spacing: 0; border-collapse: collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;\"><tr><td style=\"padding-top: 20px; padding-right: 10px; padding-bottom: 20px; padding-left: 10px\" align=\"center\"><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" href=\"https://www.smartsense.com.tr/\" style=\"height:46.5pt;width:87pt;v-text-anchor:middle;\" arcsize=\"57%\" stroke=\"false\" fillcolor=\"#00695c\"><w:anchorlock/><v:textbox inset=\"0,0,0,0\"><center style=\"color:#ffffff; font-family:'Trebuchet MS', Tahoma, sans-serif; font-size:16px\"><![endif]--><a href=\"https://www.smartsense.com.tr/\" target=\"_blank\" style=\"-webkit-text-size-adjust: none; text-decoration: none; display: inline-block; color: #ffffff; background-color: #00695c; border-radius: 35px; -webkit-border-radius: 35px; -moz-border-radius: 35px; width: auto; width: auto; border-top: 1px solid #00695c; border-right: 1px solid #00695c; border-bottom: 1px solid #00695c; border-left: 1px solid #00695c; padding-top: 15px; padding-bottom: 15px; font-family: 'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; text-align: center; mso-border-alt: none; word-break: keep-all;\"><span style=\"padding-left:30px;padding-right:30px;font-size:16px;display:inline-block;letter-spacing:undefined;\"><span style=\"font-size: 16px; margin: 0; line-height: 2; word-break: break-word; mso-line-height-alt: 32px;\"><span style=\"font-size: 16px; line-height: 32px;\" data-mce-style=\"font-size: 16px; line-height: 32px;\"><strong>DESTEK</strong></span></span></span></a>\n";
            myString = myString + "												<!--[if mso]></center></v:textbox></v:roundrect></td></tr></table><![endif]-->\n";
            myString = myString + "											</div>\n";
            myString = myString + "											<table class=\"divider\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" role=\"presentation\" valign=\"top\">\n";
            myString = myString + "												<tbody>\n";
            myString = myString + "													<tr style=\"vertical-align: top;\" valign=\"top\">\n";
            myString = myString + "														<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 30px; padding-right: 0px; padding-bottom: 0px; padding-left: 0px;\" valign=\"top\">\n";
            myString = myString + "															<table class=\"divider_content\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-top: 0px solid #BBBBBB; width: 100%;\" align=\"center\" role=\"presentation\" valign=\"top\">\n";
            myString = myString + "																<tbody>\n";
            myString = myString + "																	<tr style=\"vertical-align: top;\" valign=\"top\">\n";
            myString = myString + "																		<td style=\"word-break: break-word; vertical-align: top; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\"><span></span></td>\n";
            myString = myString + "																	</tr>\n";
            myString = myString + "																</tbody>\n";
            myString = myString + "															</table>\n";
            myString = myString + "														</td>\n";
            myString = myString + "													</tr>\n";
            myString = myString + "												</tbody>\n";
            myString = myString + "											</table>\n";
            myString = myString + "											<!--[if (!mso)&(!IE)]><!-->\n";
            myString = myString + "										</div>\n";
            myString = myString + "										<!--<![endif]-->\n";
            myString = myString + "									</div>\n";
            myString = myString + "								</div>\n";
            myString = myString + "								<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n";
            myString = myString + "								<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n";
            myString = myString + "							</div>\n";
            myString = myString + "						</div>\n";
            myString = myString + "					</div>\n";
            myString = myString + "					<div style=\"background-color:transparent;\">\n";
            myString = myString + "						<div class=\"block-grid \" style=\"min-width: 320px; max-width: 640px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; Margin: 0 auto; background-color: transparent;\">\n";
            myString = myString + "							<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n";
            myString = myString + "								<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background-color:transparent;\"><tr><td align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:640px\"><tr class=\"layout-full-width\" style=\"background-color:transparent\"><![endif]-->\n";
            myString = myString + "								<!--[if (mso)|(IE)]><td align=\"center\" width=\"640\" style=\"background-color:transparent;width:640px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top:0px; padding-bottom:0px;\"><![endif]-->\n";
            myString = myString + "								<div class=\"col num12\" style=\"min-width: 320px; max-width: 640px; display: table-cell; vertical-align: top; width: 640px;\">\n";
            myString = myString + "									<div class=\"col_cont\" style=\"width:100% !important;\">\n";
            myString = myString + "										<!--[if (!mso)&(!IE)]><!-->\n";
            myString = myString + "										<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n";
            myString = myString + "											<!--<![endif]-->\n";
            myString = myString + "											<div class=\"img-container center fixedwidth\" align=\"center\" style=\"padding-right: 0px;padding-left: 0px;\">\n";
            myString = myString + "												<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr style=\"line-height:0px\"><td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\"><![endif]--><a href=\"https://www.smartsense.com.tr/\" target=\"_blank\" style=\"outline:none\" tabindex=\"-1\"><img class=\"center fixedwidth\" align=\"center\" border=\"0\" src=\"https://www.smartsense.com.tr/img/ss-logo.PNG\" alt=\"Smartsense Logo\" title=\"Smartsense Logo\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; height: auto; border: 0; width: 160px; max-width: 100%; display: block;\" width=\"160\"></a>\n";
            myString = myString + "												<!--[if mso]></td></tr></table><![endif]-->\n";
            myString = myString + "											</div>\n";
            myString = myString + "											<!--[if (!mso)&(!IE)]><!-->\n";
            myString = myString + "										</div>\n";
            myString = myString + "										<!--<![endif]-->\n";
            myString = myString + "									</div>\n";
            myString = myString + "								</div>\n";
            myString = myString + "								<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n";
            myString = myString + "								<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n";
            myString = myString + "							</div>\n";
            myString = myString + "						</div>\n";
            myString = myString + "					</div>\n";
            myString = myString + "					<div style=\"background-color:transparent;\">\n";
            myString = myString + "						<div class=\"block-grid \" style=\"min-width: 320px; max-width: 640px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; Margin: 0 auto; background-color: #00695c;\">\n";
            myString = myString + "							<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:#00695c;\">\n";
            myString = myString + "								<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background-color:transparent;\"><tr><td align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:640px\"><tr class=\"layout-full-width\" style=\"background-color:#00695c\"><![endif]-->\n";
            myString = myString + "								<!--[if (mso)|(IE)]><td align=\"center\" width=\"640\" style=\"background-color:#00695c;width:640px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top:0px; padding-bottom:0px;\"><![endif]-->\n";
            myString = myString + "								<div class=\"col num12\" style=\"min-width: 320px; max-width: 640px; display: table-cell; vertical-align: top; width: 640px;\">\n";
            myString = myString + "									<div class=\"col_cont\" style=\"width:100% !important;\">\n";
            myString = myString + "										<!--[if (!mso)&(!IE)]><!-->\n";
            myString = myString + "										<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n";
            myString = myString + "											<!--<![endif]-->\n";
            myString = myString + "											<table class=\"social_icons\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" valign=\"top\">\n";
            myString = myString + "												<tbody>\n";
            myString = myString + "													<tr style=\"vertical-align: top;\" valign=\"top\">\n";
            myString = myString + "														<td style=\"word-break: break-word; vertical-align: top; padding-top: 20px; padding-right: 10px; padding-bottom: 10px; padding-left: 10px;\" valign=\"top\">\n";
            myString = myString + "															<table class=\"social_table\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-tspace: 0; mso-table-rspace: 0; mso-table-bspace: 0; mso-table-lspace: 0;\" valign=\"top\">\n";
            myString = myString + "																<tbody>\n";
            myString = myString + "																	<tr style=\"vertical-align: top; display: inline-block; text-align: center;\" align=\"center\" valign=\"top\">\n";
            myString = myString + "																		<td style=\"word-break: break-word; vertical-align: top; padding-bottom: 0; padding-right: 10px; padding-left: 10px;\" valign=\"top\"><a href=\"https://www.facebook.com/Smartsense-Yaz%C4%B1l%C4%B1m-106739121451383\" target=\"_blank\"><img width=\"32\" height=\"32\" src=\"https://d2fi4ri5dhpqd1.cloudfront.net/public/resources/social-networks-icon-sets/t-outline-circle-white/facebook@2x.png\" alt=\"Facebook\" title=\"Facebook\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; height: auto; border: 0; display: block;\"></a></td>\n";
            myString = myString + "																		<td style=\"word-break: break-word; vertical-align: top; padding-bottom: 0; padding-right: 10px; padding-left: 10px;\" valign=\"top\"><a href=\"https://twitter.com/SmartsenseMedia\" target=\"_blank\"><img width=\"32\" height=\"32\" src=\"https://d2fi4ri5dhpqd1.cloudfront.net/public/resources/social-networks-icon-sets/t-outline-circle-white/twitter@2x.png\" alt=\"Twitter\" title=\"Twitter\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; height: auto; border: 0; display: block;\"></a></td>\n";
            myString = myString + "																		<td style=\"word-break: break-word; vertical-align: top; padding-bottom: 0; padding-right: 10px; padding-left: 10px;\" valign=\"top\"><a href=\"https://www.instagram.com/smartsense.media/\" target=\"_blank\"><img width=\"32\" height=\"32\" src=\"https://d2fi4ri5dhpqd1.cloudfront.net/public/resources/social-networks-icon-sets/t-outline-circle-white/instagram@2x.png\" alt=\"Instagram\" title=\"Instagram\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; height: auto; border: 0; display: block;\"></a></td>\n";
            myString = myString + "																		<td style=\"word-break: break-word; vertical-align: top; padding-bottom: 0; padding-right: 10px; padding-left: 10px;\" valign=\"top\"><a href=\"https://www.youtube.com/channel/UCa1owNbtRVRuBcO77dO2YFg\" target=\"_blank\"><img width=\"32\" height=\"32\" src=\"https://d2fi4ri5dhpqd1.cloudfront.net/public/resources/social-networks-icon-sets/t-outline-circle-white/youtube@2x.png\" alt=\"YouTube\" title=\"YouTube\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; height: auto; border: 0; display: block;\"></a></td>\n";
            myString = myString + "																	</tr>\n";
            myString = myString + "																</tbody>\n";
            myString = myString + "															</table>\n";
            myString = myString + "														</td>\n";
            myString = myString + "													</tr>\n";
            myString = myString + "												</tbody>\n";
            myString = myString + "											</table>\n";
            myString = myString + "											<table class=\"divider\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" role=\"presentation\" valign=\"top\">\n";
            myString = myString + "												<tbody>\n";
            myString = myString + "													<tr style=\"vertical-align: top;\" valign=\"top\">\n";
            myString = myString + "														<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 10px; padding-right: 40px; padding-bottom: 10px; padding-left: 40px;\" valign=\"top\">\n";
            myString = myString + "															<table class=\"divider_content\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-top: 1px solid #EEEEEE; width: 100%;\" align=\"center\" role=\"presentation\" valign=\"top\">\n";
            myString = myString + "																<tbody>\n";
            myString = myString + "																	<tr style=\"vertical-align: top;\" valign=\"top\">\n";
            myString = myString + "																		<td style=\"word-break: break-word; vertical-align: top; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\"><span></span></td>\n";
            myString = myString + "																	</tr>\n";
            myString = myString + "																</tbody>\n";
            myString = myString + "															</table>\n";
            myString = myString + "														</td>\n";
            myString = myString + "													</tr>\n";
            myString = myString + "												</tbody>\n";
            myString = myString + "											</table>\n";
            myString = myString + "											<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 40px; padding-left: 40px; padding-top: 15px; padding-bottom: 20px; font-family: Tahoma, sans-serif\"><![endif]-->\n";
            myString = myString + "											<div style=\"color:#e2e2e2;font-family:Montserrat, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif;line-height:1.2;padding-top:15px;padding-right:40px;padding-bottom:20px;padding-left:40px;\">\n";
            myString = myString + "												<div class=\"txtTinyMce-wrapper\" style=\"line-height: 1.2; font-size: 12px; font-family: Montserrat, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif; color: #e2e2e2; mso-line-height-alt: 14px;\">\n";
            myString = myString + "													<p style=\"margin: 0; font-size: 14px; line-height: 1.2; word-break: break-word; text-align: center; mso-line-height-alt: 17px; margin-top: 0; margin-bottom: 0;\"><span style=\"color: #ececec;\"><span style=\"font-size: 12px;\">Smartsense Copyright © 2021</span><span style=\"font-size: 12px;\"><a href=\"http://www.example.com\" target=\"_blank\" style=\"text-decoration: underline; color: #ececec;\" rel=\"noopener\"> </a></span></span></p>\n";
            myString = myString + "												</div>\n";
            myString = myString + "											</div>\n";
            myString = myString + "											<!--[if mso]></td></tr></table><![endif]-->\n";
            myString = myString + "											<!--[if (!mso)&(!IE)]><!-->\n";
            myString = myString + "										</div>\n";
            myString = myString + "										<!--<![endif]-->\n";
            myString = myString + "									</div>\n";
            myString = myString + "								</div>\n";
            myString = myString + "								<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n";
            myString = myString + "								<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n";
            myString = myString + "							</div>\n";
            myString = myString + "						</div>\n";
            myString = myString + "					</div>\n";
            myString = myString + "					<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n";
            myString = myString + "				</td>\n";
            myString = myString + "			</tr>\n";
            myString = myString + "		</tbody>\n";
            myString = myString + "	</table>\n";
            myString = myString + "	<!--[if (IE)]></div><![endif]-->\n";
            myString = myString + "</body>\n";
            myString = myString + "\n";
            myString = myString + "</html>\n";

            if (mail.EndsWith("@outlook.com") || mail.EndsWith("@hotmail.com") || mail.EndsWith("@outlook.com.tr"))
            {
                MailMessage msg = new MailMessage();
                SmtpClient client = new SmtpClient();
                client.Credentials = new System.Net.NetworkCredential("insense.medya@outlook.com", "Sagtek21");
                client.Port = 587;
                client.Host = "smtp-mail.outlook.com";
                client.EnableSsl = true;
                msg.To.Add(mail);
                msg.From = new MailAddress("insense.medya@outlook.com", "Smartsense Şifre Değiştirilme İşlemi");
                msg.Subject = "Şifre Değiştirilme İşlemi";
                msg.Body = myString;
                msg.IsBodyHtml = true;

                client.Send(msg);
            }
            else
            {

                MailMessage msg = new MailMessage();
            SmtpClient client = new SmtpClient();
            client.Credentials = new System.Net.NetworkCredential("sagtekyazilim@gmail.com", "Sagtek21");
            client.Port = 587;
            client.Host = "smtp.gmail.com";
            client.EnableSsl = true;
            msg.To.Add(mail);
            msg.From = new MailAddress("sagtekyazilim@gmail.com", "Smartsense Şifre Değiştirilme İşlemi");
            msg.Subject = "Şifre Değiştirilme İşlemi";
                msg.IsBodyHtml = true;
                msg.Body = myString;

                client.Send(msg);
        }

        }
    }
}
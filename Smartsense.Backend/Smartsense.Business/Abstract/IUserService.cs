using System.Collections.Generic;
using Smartsense.Entity.DTO;
using Smartsense.Entity.Models;

namespace Smartsense.Business.Abstract
{
    public interface IUserService
    {
        UserRegisterResponse Register(UserRegisterRequest request);
        UserLoginResponse Login(UserLoginRequest request);
        User Get(string email);
        User Get(long id);
        RefreshTokenResponse RefreshToken(RefreshTokenRequest request);
        MailVerifyResponse Verify(MailVerifyRequest request);
        UserPutInfoResponse PutInfo(UserPutInfoRequest request);
        UserGetInfoResponse GetInfo(UserGetInfoRequest request);
        PasswordForgotResponse PasswordForgot(PasswordForgotRequest request);
        PasswordResetResponse PasswordReset(PasswordResetRequest request);
        ChangePasswordResponse ChangePassword(ChangePasswordRequest request);
        
    }
}
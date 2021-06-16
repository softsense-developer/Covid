using FluentValidation;
using Smartsense.Entity.DTO;

namespace Smartsense.Entity.Validators
{
    public class LoginValidator : AbstractValidator<UserLoginRequest>
    {
        public LoginValidator()
        {
            RuleFor(x => x.Email)
                .NotEmpty().WithMessage("Email adresi boş olamaz.")
                .Length(2, 50).WithMessage("Email adresinin uzunluğu minimum 2 maksimum 50 karakter olmalıdır.")
                .EmailAddress().WithMessage("Email adresi geçersizdir.");

            RuleFor(x => x.Password)
                .NotEmpty().WithMessage("Şifre boş olamaz.")
                .Length(6, 50).WithMessage("Şifre uzunluğu minimum 6 maksimum 50 karakter olmalıdır.");
        }
    }
}

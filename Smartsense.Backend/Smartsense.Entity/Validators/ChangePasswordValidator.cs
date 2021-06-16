using FluentValidation;
using Smartsense.Entity.DTO;

namespace Smartsense.Entity.Validators
{
    public class ChangePasswordValidator : AbstractValidator<ChangePasswordRequest>
    {
        public ChangePasswordValidator()
        {
            
            RuleFor(x => x.OldPassword)
                .NotEmpty().WithMessage("Şifre boş olamaz.")
                .Length(6, 50).WithMessage("Şifre uzunluğu minimum 6 maksimum 50 karakter olmalıdır.");
            RuleFor(x => x.NewPassword)
                .NotEmpty().WithMessage("Şifre boş olamaz.")
                .Length(6, 50).WithMessage("Şifre uzunluğu minimum 6 maksimum 50 karakter olmalıdır.");
            RuleFor(x => x.ConfirmPassword)
                .NotEmpty().WithMessage("Şifre boş olamaz.")
                .Length(6, 50).WithMessage("Şifre uzunluğu minimum 6 maksimum 50 karakter olmalıdır.");
        }
    }
}
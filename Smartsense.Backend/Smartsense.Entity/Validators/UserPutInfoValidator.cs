using FluentValidation;
using Smartsense.Entity.DTO;

namespace Smartsense.Entity.Validators
{
    public class UserPutInfoValidator : AbstractValidator<UserPutInfoRequest>
    {
        public UserPutInfoValidator()
        {
            RuleFor(x => x.Name)
                .NotEmpty().WithMessage("İsim boş olamaz.")
                .Length(2, 40).WithMessage("İsim minimum 2 maksimum 40 karakter olmalıdır");
            RuleFor(x => x.Surname)
                .NotEmpty().WithMessage("Soyisim boş olamaz.")
                .Length(2, 40).WithMessage("Soyisim minimum 2 maksimum 40 karakter olmalıdır");
            RuleFor(x => x.Phone)
                .NotEmpty().WithMessage("Telefon numarası boş olamaz.")
                .Length(10, 15).WithMessage("Telefon numarası uzunluğu minimum 10 maksimum 15 karakter olmalıdır.");


        }
    }
}
using FluentValidation;
using Smartsense.Entity.DTO;

namespace Smartsense.Entity.Validators
{
    public class PatientAddInfoValidator : AbstractValidator<PatientAddInfoRequest>
    {
        public PatientAddInfoValidator()
        {
            RuleFor(x => x.DateOfBirth)
                .NotEmpty().WithMessage("Doğum Tarihi boş olamaz.");

           

            RuleFor(x => x.IdentityNumber)
                .NotEmpty().WithMessage("TC kimlik numarası boş olamaz.");

        }
    }
}

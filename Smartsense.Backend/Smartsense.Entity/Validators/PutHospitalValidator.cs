using FluentValidation;
using Smartsense.Entity.DTO;

namespace Smartsense.Entity.Validators
{
    public class PutHospitalValidator : AbstractValidator<PutHospitalRequest>
    {
        public PutHospitalValidator()
        {
            RuleFor(x => x.Address)
                .NotEmpty().WithMessage("Hastane adresi boş olamaz.")
                .Length(2, 119).WithMessage("Hastane adresinin uzunluğu minimum 2 maksimum 119 karakter olmalıdır.");

            RuleFor(x => x.HospitalName)
                .NotEmpty().WithMessage("Hastane Adı Boş Olamaz.")
                .Length(6, 70).WithMessage("Hastane Adı uzunluğu minimum 6 maksimum 70 karakter olmalıdır.");
        }
    }
}

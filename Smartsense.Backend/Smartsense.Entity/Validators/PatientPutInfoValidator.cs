using System;
using System.Collections.Generic;
using System.Text;
using FluentValidation;
using Smartsense.Entity.DTO;

namespace Smartsense.Entity.Validators
{
    public class PatientPutInfoValidator:AbstractValidator<PatientPutInfoRequest>
    {

        public PatientPutInfoValidator()
        {
            RuleFor(x => x.DateOfBirth)
                .NotEmpty().WithMessage("Doğum Tarihi boş olamaz.");
            
            RuleFor(x => x.IdentityNumber)
                .NotEmpty().WithMessage("TC kimlik numarası boş olamaz.");

            RuleFor(x => x.Diagnosis)
                .NotEmpty().WithMessage("Teşhis boş olamaz.");
            
        }
    }
}

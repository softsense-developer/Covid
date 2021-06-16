using System;
using System.Collections.Generic;
using System.Text;
using FluentValidation;
using Smartsense.Entity.DTO;

namespace Smartsense.Entity.Validators
{
    public class PasswordForgotValidator : AbstractValidator<PasswordForgotRequest>
    {
        public PasswordForgotValidator()
        {
            RuleFor(x => x.Email)
                .NotEmpty().WithMessage("Email adresi boş olamaz.")
                .Length(2, 50).WithMessage("Email adresinin uzunluğu minimum 2 maksimum 50 karakter olmalıdır.")
                .EmailAddress().WithMessage("Email adresi geçersizdir.");
        }

    }
}

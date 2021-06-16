using FluentValidation;
using Smartsense.Entity.DTO;

namespace Smartsense.Entity.Validators
{
    public class ValueValidator : AbstractValidator<ValueRequest>
    {
        public ValueValidator()
        {
            
            //RuleFor(x => x.DataType)
            //    .NotEmpty().WithMessage("Data type boş olamaz.");

        }
    }
}

using System;
using System.Collections.Generic;
using System.Text;
using Smartsense.Entity.Enums;

namespace Smartsense.Entity.DTO
{
    public class AddWarningRequest:BaseApiRequest
    {
        public WarningType DataType { get; set; }

        public decimal WarningValue { get; set; }
    }
}

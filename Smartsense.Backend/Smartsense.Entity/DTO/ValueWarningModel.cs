using System;
using System.Collections.Generic;
using System.Text;
using Smartsense.Entity.Enums;

namespace Smartsense.Entity.DTO
{
    public class ValueWarningModel
    {
        public WarningType ValueType { get; set; }
        public decimal ValueData { get; set; }
        public DateTime time { get; set; }

    }
}

using Smartsense.Entity.Enums;
using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class WarningInfoModel
    {

        public string Name { get; set; }        
        public WarningType ValueType { get; set; }
        public decimal ValueData { get; set; }
        public DateTime time { get; set; }
    }
}

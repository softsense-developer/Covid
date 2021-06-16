using System;
using System.Collections.Generic;
using System.Text;
using Smartsense.Entity.Enums;

namespace Smartsense.Entity.DTO
{
    public class ValueRequest : BaseApiRequest
    {
        public DataValueType DataType { get; set; }
        public double DataValue { get; set; }

    }

}

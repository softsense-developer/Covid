using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class PatientValueRequest:BaseApiRequest
    {
        public long PatientId { get; set; }
    }
}

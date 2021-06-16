using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class GetPatientDataRequest:BaseApiRequest
    {
        public long PatientId { get; set; }
    }
}

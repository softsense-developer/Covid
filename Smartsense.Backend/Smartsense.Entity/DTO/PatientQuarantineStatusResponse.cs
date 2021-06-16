using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class PatientQuarantineStatusResponse:BaseApiResponse
    {
        public bool QuarantineStatus { get; set; }
    }
}

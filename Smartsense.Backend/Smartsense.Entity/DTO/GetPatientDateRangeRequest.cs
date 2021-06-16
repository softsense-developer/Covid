using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class GetPatientDateRangeRequest:BaseApiRequest
    {
        public long PatientId { get; set; }

        public DateTime OldDate { get; set; }

        public DateTime NewDate { get; set; }
    }
}

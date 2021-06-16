using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class SupervisorPromotionRequest : BaseApiRequest
    {
        public long DoctorId { get; set; }
    }
}

using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class AdminPromotionRequest:BaseApiRequest
    {
        public long PromotionId { get; set; }
        public long HospitalId { get; set; }

    }
}

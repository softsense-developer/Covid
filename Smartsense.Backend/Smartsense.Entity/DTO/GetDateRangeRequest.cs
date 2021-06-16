using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class GetDateRangeRequest :BaseApiRequest
    {
        public DateTime OldDate { get; set; }

        public DateTime NewDate { get; set; }
    }
}

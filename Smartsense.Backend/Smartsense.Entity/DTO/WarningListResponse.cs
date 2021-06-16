using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class WarningListResponse:BaseApiResponse
    {
        public List<WarningInfoModel> Warnings { get; set; }

        public WarningListResponse()
        {
            this.Warnings = new List<WarningInfoModel>();
        }

    }
}

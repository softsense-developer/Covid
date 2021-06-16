using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class GetPromotionResponse:BaseApiResponse
    {
        public List<ConnectionRequestModel> ConnectionRequests { get; set; }


        public GetPromotionResponse()
        {
            this.ConnectionRequests = new List<ConnectionRequestModel>();
        }
    }
}

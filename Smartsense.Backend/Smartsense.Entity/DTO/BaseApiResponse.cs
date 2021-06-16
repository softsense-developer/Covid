using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class BaseApiResponse
    {
        public string Code { get; set; }
        public string Message { get; set; }
        
        public List<string> Errors { get; set; }

        public BaseApiResponse()
        {
            this.Errors = new List<string>();
        }
    }
}

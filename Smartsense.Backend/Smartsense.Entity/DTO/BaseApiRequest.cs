using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class BaseApiRequest
    {
        [System.Text.Json.Serialization.JsonIgnore]
        public long UserId { get; set; }
    }
}

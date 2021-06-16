using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class UserPutInfoRequest : BaseApiRequest
    {
        public string Name { get; set; }

        public string Surname { get; set; }

        public string Phone { get; set; }

       
    }
}

using System;
using System.Collections.Generic;
using System.Text;

namespace Smartsense.Entity.DTO
{
    public class ConnectionAcceptRefuseRequest:BaseApiRequest
    {
        public long Id { get; set; }

        public bool isAccept { get; set; }

    }
}

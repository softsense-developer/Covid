﻿using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text;
using Smartsense.Core.Entities;

namespace Smartsense.Entity.Models
{
    [Table("mail_verify")]
    public class MailVerify : BaseEntity
    {
        [Column("user_id")]
        public long UserId { get; set; }

        [Column("guid")]
        public string Guid { get; set; }
    }
}

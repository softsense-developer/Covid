using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text;
using Smartsense.Core.Entities;

namespace Smartsense.Entity.Models
{
    [Table("connections")]
    public class Connection :BaseEntity
    {
        [Column("child_user_id")]
        public long ChildUserId { get; set; }

        [Column("parent_user_id")]
        public long ParentUserId { get; set; }

        [Column("request_status")]
        public bool RequestStatus { get; set; }
        
    }
}

using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text;
using Smartsense.Core.Entities;

namespace Smartsense.Entity.Models
{
    [Table("roles")]
    public class Role:BaseEntity
    {
        [Column("role_name")]
        public string RoleName { get; set; }
    }
}

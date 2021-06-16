using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text;
using Smartsense.Core.Entities;

namespace Smartsense.Entity.Models
{
    [Table("location_warnings")]
    public class LocationWarning:BaseEntity
    {
        [Column("user_id")]
        public long UserId { get; set; }

        [Column("latitude_warning")]
        public decimal LatitudeWarning { get; set; }

        [Column("longitude_warning")]
        public decimal LongitudeWarning { get; set; }

        [Column("range")]
        public decimal Range { get; set; }
    }
}

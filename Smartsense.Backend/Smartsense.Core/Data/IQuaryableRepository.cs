using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Smartsense.Core.Entities;
namespace Smartsense.Core.Data.Ef
{
    public interface IQuaryableRepository<T> where T : class, IEntity, new()
    {
        IQueryable<T> Table { get; }

    }
}

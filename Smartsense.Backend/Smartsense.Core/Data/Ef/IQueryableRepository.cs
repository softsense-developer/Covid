using Smartsense.Core.Entities;

namespace Smartsense.Core.Data.Ef
{
    public interface IQueryableRepository<T> where T : class, IEntity, new()
    {
    }
}
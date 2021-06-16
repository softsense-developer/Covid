using System;
using System.Collections.Generic;
using System.Text;
using Smartsense.Entity.DTO;

namespace Smartsense.Business.Abstract
{
    public interface IAdminService
    {
        public AddHospitalResponse AddHospital(AddHospitalRequest request);

        AdminPromotionResponse SupervisorPromotion (AdminPromotionRequest request);

        AdminDemotionResponse SupervisorDemotion(AdminDemotionRequest request);

        GetAllHospitalResponse GetAllHospital(GetAllHospitalRequest request);

        GetAllUsersResponse GetAllUsers(GetAllUsersRequest request);
    }
}

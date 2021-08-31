using System.Collections.Generic;
using Smartsense.Entity.DTO;
using Smartsense.Entity.Models;

namespace Smartsense.Business.Abstract
{
    public interface IPatientService
    {
        PatientAddInfoResponse AddInfo (PatientAddInfoRequest request);

        PatientGetInfoResponse GetInfo(PatientGetInfoRequest request);

        PatientPutInfoResponse PutInfo(PatientPutInfoRequest request);

        PatientRelationDoctorResponse AddDoctor(PatientRelationDoctorRequest request);

        LocationPostResponse AddQuarantineLocation (LocationPostRequest request);

        LocationPutResponse PutNowLocation(LocationPutRequest request);

        PatientGetLocationResponse GetPatientLocation(PatientGetLocationRequest request);

        PatientQuarantineStatusResponse QuarantineStatus(PatientQuarantineStatusRequest request);

        AddWarningResponse AddWarning (AddWarningRequest request);

        GetHospitalResponse GetHospital();

        GetHospitalDoctorsResponse GetDoctors(long id);

        AddCompanionResponse AddCompanion(AddCompanionRequest request);

        GetCompanionResponse ListCompanion(GetCompanionRequest request);

        DeleteCompanionResponse DeleteCompanion(DeleteCompanionRequest request);

        GetPromotionResponse GetPromotion(GetPromotionRequest request);

        ConnectionAcceptRefuseResponse ConnectionAcceptRefuse(ConnectionAcceptRefuseRequest request);

        AudioTestResponse AudioTest(AudioTestRequest request);

    }
}
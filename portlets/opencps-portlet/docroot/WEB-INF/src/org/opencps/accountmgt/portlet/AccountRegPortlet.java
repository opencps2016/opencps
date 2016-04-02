
package org.opencps.accountmgt.portlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.opencps.accountmgt.model.Business;
import org.opencps.accountmgt.model.Citizen;
import org.opencps.accountmgt.search.BusinessDisplayTerms;
import org.opencps.accountmgt.search.CitizenDisplayTerms;
import org.opencps.accountmgt.service.BusinessLocalServiceUtil;
import org.opencps.accountmgt.service.CitizenLocalServiceUtil;
import org.opencps.datamgt.NoSuchDictItemException;
import org.opencps.datamgt.model.DictItem;
import org.opencps.datamgt.service.DictItemLocalServiceUtil;
import org.opencps.util.DateTimeUtil;
import org.opencps.util.PortletUtil;
import org.opencps.util.WebKeys;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class AccountRegPortlet extends MVCPortlet {

	@Override
	public void render(
	    RenderRequest renderRequest, RenderResponse renderResponse)
	    throws PortletException, IOException {

		long citizenId =
		    ParamUtil.getLong(renderRequest, CitizenDisplayTerms.CITIZEN_ID);

		long businessId =
		    ParamUtil.getLong(
		        renderRequest, BusinessDisplayTerms.BUSINESS_BUSINESSID);

		try {
			if (citizenId > 0) {
				Citizen citizen =
				    CitizenLocalServiceUtil.fetchCitizen(citizenId);
				renderRequest.setAttribute(WebKeys.CITIZEN_ENTRY, citizen);
			}

			if (businessId > 0) {
				Business business =
				    BusinessLocalServiceUtil.fetchBusiness(businessId);
				renderRequest.setAttribute(WebKeys.BUSINESS_ENTRY, business);
			}
		}

		catch (Exception e) {
			_log.error(e);
		}

		super.render(renderRequest, renderResponse);
	}

	public void updateBusiness(
	    ActionRequest actionRequest, ActionResponse actionResponse)

	    throws FileNotFoundException {

		long businessId =
		    ParamUtil.getLong(
		        actionRequest, BusinessDisplayTerms.BUSINESS_BUSINESSID);
		String name =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_NAME);
		String enName =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_ENNAME);
		String idNumber =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_IDNUMBER);
		String shortName =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_SHORTNAME);
		String type =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_BUSINESSTYPE);
		String address =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_ADDRESS);
		long cityId =
		    ParamUtil.getLong(
		        actionRequest, BusinessDisplayTerms.BUSINESS_CITY_ID);
		long districtId =
		    ParamUtil.getLong(
		        actionRequest, BusinessDisplayTerms.BUSINESS_DISTRICT_ID);
		long wardId =
		    ParamUtil.getLong(
		        actionRequest, BusinessDisplayTerms.BUSINESS_WARD_ID);
		String email =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_EMAIL);
		String telNo =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_TELNO);
		String representativeName =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_REPRESENTATIVENAME);
		String representativeRole =
		    ParamUtil.getString(
		        actionRequest, BusinessDisplayTerms.BUSINESS_REPRESENTATIVEROLE);

		String[] domain =
		    ParamUtil.getParameterValues(
		        actionRequest, BusinessDisplayTerms.BUSINESS_DOMAIN);

		UploadPortletRequest uploadPortletRequest =
		    PortalUtil.getUploadPortletRequest(actionRequest);

		File attachFile = (File) uploadPortletRequest.getFile("attachFile");

		InputStream inputStream = new FileInputStream(attachFile);

		String mimeType = MimeTypesUtil.getContentType(attachFile);

		Date defaultBirthDate = DateTimeUtil.convertStringToDate("01/01/1970");
		PortletUtil.SplitDate spd = new PortletUtil.SplitDate(defaultBirthDate);

		ServiceContext serviceContext;

		DictItem city = null;

		DictItem district = null;

		DictItem ward = null;

		try {
			serviceContext = ServiceContextFactory.getInstance(actionRequest);
			city = DictItemLocalServiceUtil.getDictItem(cityId);
			district = DictItemLocalServiceUtil.getDictItem(districtId);
			ward = DictItemLocalServiceUtil.getDictItem(wardId);

			if (businessId == 0) {

				BusinessLocalServiceUtil.addBusiness(
				    name, enName, shortName, type, idNumber, address,
				    city.getItemCode(), district.getItemCode(),
				    ward.getItemCode(),
				    city.getItemName(serviceContext.getLocale(), true),
				    district.getItemName(serviceContext.getLocale(), true),
				    ward.getItemName(serviceContext.getLocale(), true), telNo,
				    email, representativeName, representativeRole, domain,
				    spd.getDayOfMoth(), spd.getMonth(), spd.getYear(),
				    serviceContext.getScopeGroupId(), attachFile.getName(),
				    mimeType, StringPool.BLANK, inputStream,
				    attachFile.getTotalSpace(), serviceContext);
			}
			else {
				/* BusinessLocalServiceUtil.updateBusiness(business); */
			}
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	public void updateCitizen(
	    ActionRequest actionRequest, ActionResponse actionResponse)
	    throws SystemException, FileNotFoundException, NoSuchDictItemException,
	    PortalException {

		    ParamUtil.getLong(actionRequest, "dictCollectionId");
		long citizenId =
		    ParamUtil.getLong(actionRequest, CitizenDisplayTerms.CITIZEN_ID);
		String fullName =
		    ParamUtil.getString(
		        actionRequest, CitizenDisplayTerms.CITIZEN_FULLNAME);
		String personId =
		    ParamUtil.getString(
		        actionRequest, CitizenDisplayTerms.CITIZEN_PERSONALID);
		String adress =
		    ParamUtil.getString(
		        actionRequest, CitizenDisplayTerms.CITIZEN_ADDRESS);
		long cityId =
		    ParamUtil.getLong(
		        actionRequest, CitizenDisplayTerms.CITIZEN_CITY_ID);
		long districtId =
		    ParamUtil.getLong(
		        actionRequest, CitizenDisplayTerms.CITIZEN_DISTRICT_ID);
		long wardId =
		    ParamUtil.getLong(
		        actionRequest, CitizenDisplayTerms.CITIZEN_WARD_ID);
		String email =
		    ParamUtil.getString(
		        actionRequest, CitizenDisplayTerms.CITIZEN_EMAIL);
		String telNo =
		    ParamUtil.getString(
		        actionRequest, CitizenDisplayTerms.CITIZEN_TELNO);
		int birthDateDay =
		    ParamUtil.getInteger(
		        actionRequest, CitizenDisplayTerms.BIRTH_DATE_DAY);
		int birthDateMonth =
		    ParamUtil.getInteger(
		        actionRequest, CitizenDisplayTerms.BIRTH_DATE_MONTH);
		int birthDateYear =
		    ParamUtil.getInteger(
		        actionRequest, CitizenDisplayTerms.BIRTH_DATE_YEAR);
		int gender =
		    ParamUtil.getInteger(
		        actionRequest, CitizenDisplayTerms.CITIZEN_GENDER);
		UploadPortletRequest uploadPortletRequest =
		    PortalUtil.getUploadPortletRequest(actionRequest);

		File attachFile = (File) uploadPortletRequest.getFile("attachFile");

		InputStream inputStream = new FileInputStream(attachFile);
		
		String mimeType = MimeTypesUtil.getContentType(attachFile);
		DictItem city = null;

		DictItem district = null;

		DictItem ward = null;
		try {
			ServiceContext serviceContext =
			    ServiceContextFactory.getInstance(actionRequest);
			city = DictItemLocalServiceUtil.getDictItem(cityId);
			district = DictItemLocalServiceUtil.getDictItem(districtId);
			ward = DictItemLocalServiceUtil.getDictItem(wardId);
			if (citizenId == 0) {
				CitizenLocalServiceUtil.addCitizen(
				    fullName, personId, gender, birthDateDay, birthDateMonth,
				    birthDateYear, adress, city.getItemCode(), district.getItemCode(),
				    ward.getItemCode(),
				    city.getItemName(serviceContext.getLocale(), true),
				    district.getItemName(serviceContext.getLocale(), true),
				    ward.getItemName(serviceContext.getLocale(), true), email,
				    telNo, serviceContext.getScopeGroupId(),
				    attachFile.getName(), mimeType, StringPool.BLANK,
				    inputStream, attachFile.getTotalSpace(), serviceContext);
			}
			else {
				/* CitizenLocalServiceUtil.updateCitizen(citizen) */
			}
		}
		catch (Exception e) {
			_log.error(e);
		}

	}

	private Log _log = LogFactoryUtil.getLog(AccountRegPortlet.class.getName());
}

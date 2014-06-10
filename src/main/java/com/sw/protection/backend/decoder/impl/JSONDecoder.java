package com.sw.protection.backend.decoder.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sw.protection.backend.common.exception.DecodingException;
import com.sw.protection.backend.config.ObjectType;
import com.sw.protection.backend.decoder.Decoder;
import com.sw.protection.backend.entity.Admin;
import com.sw.protection.backend.entity.AdminScope;
import com.sw.protection.backend.entity.Company;
import com.sw.protection.backend.entity.CompanyClient;
import com.sw.protection.backend.entity.CompanySW;
import com.sw.protection.backend.entity.CompanySWCopy;
import com.sw.protection.backend.entity.CompanyUser;
import com.sw.protection.backend.entity.CompanyUserScope;
import com.sw.protection.backend.entity.SuperAdmin;
import com.sw.protection.backend.entity.Trace;
import com.sw.protection.backend.entity.UsageData;

/**
 * Implementation of JSON Decoder which is used to decode the any given JSON
 * string
 * 
 * @author dinuka
 * 
 */
public class JSONDecoder implements Decoder {

	public static final Logger log = Logger.getLogger(JSONDecoder.class.getName());

	@Override
	public Object decodeObject(ObjectType objectType, String encodedString)
			throws DecodingException {
		switch (objectType) {
		case SUPER_ADMIN:
			try {
				SuperAdmin superAdmin = null;
				superAdmin = this.decodeSuperAdmin(encodedString);
				return superAdmin;
			} catch (ClassCastException ex) {
				log.error(ex);
				throw ex;
			}

		case ADMIN:
			try {
				Admin admin = null;
				admin = this.decodeAdmin(encodedString);
				return admin;
			} catch (ClassCastException ex) {
				log.error(ex);
				throw ex;
			}

		case COMPANY:
			try {
				Company company = null;
				company = this.decodeCompany(encodedString);
				return company;
			} catch (ClassCastException ex) {
				log.error(ex);
				throw ex;
			}

		case COMPANY_USER:
			try {
				CompanyUser companyUser = null;
				companyUser = this.decodeCompanyUser(encodedString);
				return companyUser;
			} catch (ClassCastException ex) {
				log.error(ex);
				throw ex;
			}

		case CLIENT:
			try {
				CompanyClient companyClient = null;
				companyClient = this.decodeCompanyClient(encodedString);
				return companyClient;
			} catch (ClassCastException ex) {
				log.error(ex);
				throw ex;
			}

		case SOFTWARE:
			try {
				CompanySW companySW = null;
				companySW = this.decodeCompanySoftware(encodedString);
				return companySW;
			} catch (ClassCastException ex) {
				log.error(ex);
				throw ex;
			}

		case SOFTWARE_COPY:
			try {
				CompanySWCopy companySWCopy = null;
				companySWCopy = this.decodeCompanySoftwareCopy(encodedString);
				return companySWCopy;
			} catch (ClassCastException ex) {
				log.error(ex);
				throw ex;
			}

		case ADMIN_SCOPE:
			try {
				AdminScope adminScope = null;
				adminScope = this.decodeAdminScope(encodedString);
				return adminScope;
			} catch (ClassCastException ex) {
				log.error(ex);
				throw ex;
			}

		case COMPANY_USER_SCOPE:
			try {
				CompanyUserScope companyUserScope = null;
				companyUserScope = this.decodeCompanyUserScope(encodedString);
				return companyUserScope;
			} catch (ClassCastException ex) {
				log.error(ex);
				throw ex;
			}

		case TRACE:
			try {
				Trace trace = null;
				trace = this.decodeTrace(encodedString);
				return trace;
			} catch (ClassCastException ex) {
				log.error(ex);
				throw ex;
			}

		case USAGE:
			try {
				UsageData usageData = null;
				usageData = this.decodeUsage(encodedString);
				return usageData;
			} catch (ClassCastException ex) {
				log.error(ex);
				throw ex;
			}

		}
		return null;
	}

	@Override
	public List<?> decodeObjectList(ObjectType objectType, String encodedString)
			throws DecodingException {
		switch (objectType) {
		case SUPER_ADMIN:
			try {
				List<SuperAdmin> superAdmin = null;
				superAdmin = this.decodeSuperAdminList(encodedString);
				return superAdmin;
			} catch (Exception ex) {
				log.error(ex);
				throw new DecodingException();
			}

		case ADMIN:
			try {
				List<Admin> admin = null;
				admin = this.decodeAdminList(encodedString);
				return admin;
			} catch (ClassCastException ex) {
				log.error(ex);
				throw ex;
			}

		case COMPANY:
			try {
				List<Company> company = null;
				company = this.decodeCompanyList(encodedString);
				return company;
			} catch (ClassCastException ex) {
				log.error(ex);
				throw ex;
			}

		case COMPANY_USER:
			try {
				List<CompanyUser> companyUser = null;
				companyUser = this.decodeCompanyUserList(encodedString);
				return companyUser;
			} catch (ClassCastException ex) {
				log.error(ex);
				throw ex;
			}

		case CLIENT:
			try {
				List<CompanyClient> companyClient = null;
				companyClient = this.decodeCompanyClientList(encodedString);
				return companyClient;
			} catch (ClassCastException ex) {
				log.error(ex);
				throw ex;
			}

		case SOFTWARE:
			try {
				List<CompanySW> companySW = null;
				companySW = this.decodeCompanySoftwareList(encodedString);
				return companySW;
			} catch (ClassCastException ex) {
				log.error(ex);
				throw ex;
			}

		case SOFTWARE_COPY:
			try {
				List<CompanySWCopy> companySWCopy = null;
				companySWCopy = this.decodeCompanySoftwareCopyList(encodedString);
				return companySWCopy;
			} catch (ClassCastException ex) {
				log.error(ex);
				throw ex;
			}

		case ADMIN_SCOPE:
			try {
				List<AdminScope> adminScope = null;
				adminScope = this.decodeAdminScopeList(encodedString);
				return adminScope;
			} catch (ClassCastException ex) {
				log.error(ex);
				throw ex;
			}

		case COMPANY_USER_SCOPE:
			try {
				List<CompanyUserScope> companyUserScope = null;
				companyUserScope = this.decodeCompanyUserScopeList(encodedString);
				return companyUserScope;
			} catch (ClassCastException ex) {
				log.error(ex);
				throw ex;
			}

		case TRACE:
			try {
				List<Trace> trace = null;
				trace = this.decodeTraceList(encodedString);
				return trace;
			} catch (ClassCastException ex) {
				log.error(ex);
				throw ex;
			}

		case USAGE:
			try {
				List<UsageData> usageData = null;
				usageData = this.decodeUsageList(encodedString);
				return usageData;
			} catch (ClassCastException ex) {
				log.error(ex);
				throw ex;
			}

		}
		return null;
	}

	private SuperAdmin decodeSuperAdmin(String encodeString) throws DecodingException {
		Gson gson = new Gson();
		SuperAdmin superAdmin = null;
		try {
			superAdmin = gson.fromJson(encodeString, SuperAdmin.class);
		} catch (Exception e) {
			log.error(e);
			throw new DecodingException("Provided JSON string cannot decode to SuperAdmin");
		}
		return superAdmin;
	}

	private List<SuperAdmin> decodeSuperAdminList(String encodeString) throws DecodingException {
		Gson gson = new Gson();
		List<SuperAdmin> adminList = null;
		try {
			adminList = gson.fromJson(encodeString, new TypeToken<ArrayList<SuperAdmin>>() {
			}.getType());
		} catch (Exception e) {
			log.error(e);
			throw new DecodingException("Provided JSON string cannot decode to list of SuperAdmins");
		}
		return adminList;
	}

	private Admin decodeAdmin(String encodeString) throws DecodingException {
		Gson gson = new Gson();
		Admin admin = null;
		try {
			admin = gson.fromJson(encodeString, Admin.class);
		} catch (Exception e) {
			log.error(e);
			throw new DecodingException("Provided JSON string cannot decode to Admin");
		}
		return admin;
	}

	private List<Admin> decodeAdminList(String encodeString) throws DecodingException {
		Gson gson = new Gson();
		List<Admin> adminList = null;
		try {
			adminList = gson.fromJson(encodeString, new TypeToken<ArrayList<Admin>>() {
			}.getType());
		} catch (Exception e) {
			log.error(e);
			throw new DecodingException("Provided JSON string cannot decode to list of Admins");
		}
		return adminList;
	}

	private Company decodeCompany(String encodeString) throws DecodingException {
		Gson gson = new Gson();
		Company company = null;
		try {
			company = gson.fromJson(encodeString, Company.class);
		} catch (Exception e) {
			log.error(e);
			throw new DecodingException("Provided JSON string cannot decode to Company");
		}
		return company;
	}

	private List<Company> decodeCompanyList(String encodeString) throws DecodingException {
		Gson gson = new Gson();
		List<Company> companyList = null;
		try {
			companyList = gson.fromJson(encodeString, new TypeToken<ArrayList<Company>>() {
			}.getType());
		} catch (Exception e) {
			log.error(e);
			throw new DecodingException("Provided JSON string cannot decode to list of Companies");
		}
		return companyList;
	}

	private CompanyUser decodeCompanyUser(String encodeString) throws DecodingException {
		Gson gson = new Gson();
		CompanyUser companyUser = null;
		try {
			companyUser = gson.fromJson(encodeString, CompanyUser.class);
		} catch (Exception e) {
			log.error(e);
			throw new DecodingException("Provided JSON string cannot decode to CompanyUser");
		}
		return companyUser;
	}

	private List<CompanyUser> decodeCompanyUserList(String encodeString) throws DecodingException {
		Gson gson = new Gson();
		List<CompanyUser> companyUserList = null;
		try {
			companyUserList = gson.fromJson(encodeString, new TypeToken<ArrayList<CompanyUser>>() {
			}.getType());
		} catch (Exception e) {
			log.error(e);
			throw new DecodingException(
					"Provided JSON string cannot decode to list of CompanyUsers");
		}
		return companyUserList;
	}

	private CompanyClient decodeCompanyClient(String encodeString) throws DecodingException {
		Gson gson = new Gson();
		CompanyClient companyClient = null;
		try {
			companyClient = gson.fromJson(encodeString, CompanyClient.class);
		} catch (Exception e) {
			log.error(e);
			throw new DecodingException("Provided JSON string cannot decode to CompanyClient");
		}
		return companyClient;
	}

	private List<CompanyClient> decodeCompanyClientList(String encodeString)
			throws DecodingException {
		Gson gson = new Gson();
		List<CompanyClient> companyClientList = null;
		try {
			companyClientList = gson.fromJson(encodeString,
					new TypeToken<ArrayList<CompanyClient>>() {
					}.getType());
		} catch (Exception e) {
			log.error(e);
			throw new DecodingException(
					"Provided JSON string cannot decode to list of CompanyClients");
		}
		return companyClientList;
	}

	private CompanySW decodeCompanySoftware(String encodeString) throws DecodingException {
		Gson gson = new Gson();
		CompanySW companySoftware = null;
		try {
			companySoftware = gson.fromJson(encodeString, CompanySW.class);
		} catch (Exception e) {
			log.error(e);
			throw new DecodingException("Provided JSON string cannot decode to CompanySoftware");
		}
		return companySoftware;
	}

	private List<CompanySW> decodeCompanySoftwareList(String encodeString) throws DecodingException {
		Gson gson = new Gson();
		List<CompanySW> companySoftwareList = null;
		try {
			companySoftwareList = gson.fromJson(encodeString,
					new TypeToken<ArrayList<CompanySW>>() {
					}.getType());
		} catch (Exception e) {
			log.error(e);
			throw new DecodingException(
					"Provided JSON string cannot decode to list of CompanySoftwares");
		}
		return companySoftwareList;
	}

	private CompanySWCopy decodeCompanySoftwareCopy(String encodeString) throws DecodingException {
		Gson gson = new Gson();
		CompanySWCopy companySoftwareCopy = null;
		try {
			companySoftwareCopy = gson.fromJson(encodeString, CompanySWCopy.class);
		} catch (Exception e) {
			log.error(e);
			throw new DecodingException("Provided JSON string cannot decode to CompanySoftwareCopy");
		}
		return companySoftwareCopy;
	}

	private List<CompanySWCopy> decodeCompanySoftwareCopyList(String encodeString)
			throws DecodingException {
		Gson gson = new Gson();
		List<CompanySWCopy> companySoftwareCopyList = null;
		try {
			companySoftwareCopyList = gson.fromJson(encodeString,
					new TypeToken<ArrayList<CompanySWCopy>>() {
					}.getType());
		} catch (Exception e) {
			log.error(e);
			throw new DecodingException(
					"Provided JSON string cannot decode to list of CompanySoftwareCopies");
		}
		return companySoftwareCopyList;
	}

	private AdminScope decodeAdminScope(String encodeString) throws DecodingException {
		Gson gson = new Gson();
		AdminScope adminScope = null;
		try {
			adminScope = gson.fromJson(encodeString, AdminScope.class);
		} catch (Exception e) {
			log.error(e);
			throw new DecodingException("Provided JSON string cannot decode to AdminScope");
		}
		return adminScope;
	}

	private List<AdminScope> decodeAdminScopeList(String encodeString) throws DecodingException {
		Gson gson = new Gson();
		List<AdminScope> adminScopeList = null;
		try {
			adminScopeList = gson.fromJson(encodeString, new TypeToken<ArrayList<AdminScope>>() {
			}.getType());
		} catch (Exception e) {
			log.error(e);
			throw new DecodingException("Provided JSON string cannot decode to list of AdminScopes");
		}
		return adminScopeList;
	}

	private CompanyUserScope decodeCompanyUserScope(String encodeString) throws DecodingException {
		Gson gson = new Gson();
		CompanyUserScope companyUserScope = null;
		try {
			companyUserScope = gson.fromJson(encodeString, CompanyUserScope.class);
		} catch (Exception e) {
			log.error(e);
			throw new DecodingException("Provided JSON string cannot decode to CompanyUserScope");
		}
		return companyUserScope;
	}

	private List<CompanyUserScope> decodeCompanyUserScopeList(String encodeString)
			throws DecodingException {
		Gson gson = new Gson();
		List<CompanyUserScope> companyUserScopeList = null;
		try {
			companyUserScopeList = gson.fromJson(encodeString,
					new TypeToken<ArrayList<CompanyUserScope>>() {
					}.getType());
		} catch (Exception e) {
			log.error(e);
			throw new DecodingException(
					"Provided JSON string cannot decode to list of CompanyUserScopes");
		}
		return companyUserScopeList;
	}

	private Trace decodeTrace(String encodeString) throws DecodingException {
		Gson gson = new Gson();
		Trace trace = null;
		try {
			trace = gson.fromJson(encodeString, Trace.class);
		} catch (Exception e) {
			log.error(e);
			throw new DecodingException("Provided JSON string cannot decode to Trace");
		}
		return trace;
	}

	private List<Trace> decodeTraceList(String encodeString) throws DecodingException {
		Gson gson = new Gson();
		List<Trace> trace = null;
		try {
			trace = gson.fromJson(encodeString, new TypeToken<ArrayList<Trace>>() {
			}.getType());
		} catch (Exception e) {
			log.error(e);
			throw new DecodingException("Provided JSON string cannot decode to list of Trace");
		}
		return trace;
	}

	private UsageData decodeUsage(String encodeString) throws DecodingException {
		Gson gson = new Gson();
		UsageData usage = null;
		try {
			usage = gson.fromJson(encodeString, UsageData.class);
		} catch (Exception e) {
			log.error(e);
			throw new DecodingException("Provided JSON string cannot decode to UsageData");
		}
		return usage;
	}

	private List<UsageData> decodeUsageList(String encodeString) throws DecodingException {
		Gson gson = new Gson();
		List<UsageData> usage = null;
		try {
			usage = gson.fromJson(encodeString, new TypeToken<ArrayList<UsageData>>() {
			}.getType());
		} catch (Exception e) {
			log.error(e);
			throw new DecodingException("Provided JSON string cannot decode to list of UsageData");
		}
		return usage;
	}
}

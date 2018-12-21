package fr.insee.omphale.core.service.administration.utilisateurs.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "cn", "uid", "mails" , "ou", "string" })
class Personne {

	private String cn, uid,  ou, string;
    @XmlElementWrapper(name="mail")
    @XmlElement(name="string")
	protected List<String> mails = new ArrayList<String>();

	public String getMail() {
		return mails.get(0);
	}

	public void setMails(List<String> mails) {
		this.mails = mails;
	}

	@XmlElement(name = "cn")
	public String getCn() {
		return cn;
	}

	@XmlElement(name = "uid")
	public String getUid() {
		return uid;
	}

	@XmlElement(name = "ou")
	public String getOu() {
		return ou;
	}
	
	@XmlElement(name = "string")
	public String getString() {
		return string;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setOu(String ou) {
		this.ou = ou;
	}
	
	public void setString(String string) {
		this.string = string;
	}
}


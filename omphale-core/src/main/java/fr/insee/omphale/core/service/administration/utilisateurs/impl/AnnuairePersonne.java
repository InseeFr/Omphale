package fr.insee.omphale.core.service.administration.utilisateurs.impl;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "cn", "uid", "mail", "ou" })
public class AnnuairePersonne {

	private String cn, uid, mail, ou;

	@XmlElement(name = "cn")
	public String getCn() {
		return cn;
	}

	@XmlElement(name = "uid")
	public String getUid() {
		return uid;
	}

	@XmlElement(name = "mail")
	public String getMail() {
		return mail;
	}

	@XmlElement(name = "ou")
	public String getOu() {
		return ou;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setOu(String ou) {
		this.ou = ou;
	}
}

package domain;

import exception.domain.DomainException;

/**
 * This class provides an unique EAN. This must contain of 13 ciphers.
 * 
 * @author Pieter Declercq
 * 
 */
public class Identifier {

	/**
	 * The barcode which consists of 13 ciphers.
	 */
	private Long ean;

	/**
	 * This method returns the EAN.
	 * 
	 * @return The EAN
	 */
	public Long getEan() {
		return ean;
	}

	/**
	 * A method which sets the EAN to a given Long of 8 or 13 ciphers.
	 * 
	 * @param ean
	 *            The new EAN
	 * @throws DomainException
	 *             If the ean doesn't consist of 8 or 13 ciphers
	 */
	public void setEan(Long ean) throws DomainException {
		if (ean < 1000000000000L || ean > 9999999999999L) { // EAN13
			if (ean < 10000000L || ean > 99999999L) { //EAN8
				throw new DomainException(
						"The EAN doesn't have exact 8 or 13 ciphers");
			}
		}
		this.ean = ean;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ean == null) ? 0 : ean.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Identifier other = (Identifier) obj;
		if (ean == null) {
			if (other.ean != null)
				return false;
		} else if (!ean.equals(other.ean))
			return false;
		return true;
	}

}
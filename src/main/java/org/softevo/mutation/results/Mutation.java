package org.softevo.mutation.results;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Mutation {

	@Id
	@GeneratedValue
	private Long id;

	private static final String PREFIX = "mutationId";

	static int mutionIdGenerator = 0;

	public enum MutationType {
		NO_MUTATION, RIC_PLUS_1, RIC_MINUS_1, RIC_ZERO, NEGATE_JUMP, ARITHMETIC_REPLACE
	};

	private String className;

	private int lineNumber;

	private int mutationForLine;

	private MutationType mutationType;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private SingleTestResult mutationResult;

	private Mutation() {

	}

	public Mutation(String className, int line, int mutationForLine,
			MutationType mutation) {
		super();
		if (className == null || line < 0 || mutation == null) {
			throw new IllegalArgumentException(String.format(
					"Arguments were: %s - %d - %s", className, line, mutation));
		}
		if (className.contains("/")) {
			className = className.replace('/', '.');
		}
		this.className = className;
		this.lineNumber = line;
		this.mutationForLine = mutationForLine;
		this.mutationType = mutation;
	}

	@Override
	public String toString() {
		return String.format("%s - %d (%d)- %s %s", className, lineNumber,
				mutationForLine, mutationType.toString(),
				mutationResult == null ? "No Result" : mutationResult
						.toString());
	}

	public String getClassName() {
		return className;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public String getMutationVariable() {
		return PREFIX + "_" + id;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the mutationType
	 */
	public MutationType getMutationType() {
		return mutationType;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @param lineNumber
	 *            the lineNumber to set
	 */
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	/**
	 * @param mutationType
	 *            the mutationType to set
	 */
	public void setMutationType(MutationType mutationType) {
		this.mutationType = mutationType;
	}

	/**
	 * @return the mutationResult
	 */
	public SingleTestResult getMutationResult() {
		return mutationResult;
	}

	/**
	 * @param mutationTestResult
	 *            the mutationResult to set
	 */
	public void setMutationResult(SingleTestResult mutationTestResult) {
		this.mutationResult = mutationTestResult;
	}

	/**
	 * @return the mutationForLine
	 */
	public int getMutationForLine() {
		return mutationForLine;
	}

	/**
	 * @param mutationForLine
	 *            the mutationForLine to set
	 */
	public void setMutationForLine(int mutationForLine) {
		this.mutationForLine = mutationForLine;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((className == null) ? 0 : className.hashCode());
		result = PRIME * result + ((id == null) ? 0 : id.hashCode());
		result = PRIME * result + lineNumber;
		result = PRIME * result + mutationForLine;
		result = PRIME * result + ((mutationResult == null) ? 0 : mutationResult.hashCode());
		result = PRIME * result + ((mutationType == null) ? 0 : mutationType.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Mutation other = (Mutation) obj;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lineNumber != other.lineNumber)
			return false;
		if (mutationForLine != other.mutationForLine)
			return false;
		if (mutationResult == null) {
			if (other.mutationResult != null)
				return false;
		} else if (!mutationResult.equals(other.mutationResult))
			return false;
		if (mutationType == null) {
			if (other.mutationType != null)
				return false;
		} else if (!mutationType.equals(other.mutationType))
			return false;
		return true;
	}

}

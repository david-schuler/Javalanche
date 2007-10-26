package org.softevo.mutation.bytecodeMutations.replaceIntegerConstant;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.softevo.mutation.bytecodeMutations.BytecodeTasks;
import org.softevo.mutation.bytecodeMutations.AbstractMutationAdapter;
import org.softevo.mutation.bytecodeMutations.MutationCode;
import org.softevo.mutation.results.Mutation;
import org.softevo.mutation.results.Mutation.MutationType;
import org.softevo.mutation.results.persistence.MutationManager;
import org.softevo.mutation.results.persistence.QueryManager;

public class RicMethodAdapter extends AbstractMutationAdapter {

	private static class ConstantMutations {

		private Mutation plus1;

		private Mutation minus1;

		private Mutation zero;

		public ConstantMutations(Mutation plus1, Mutation minus1, Mutation zero) {
			super();
			this.plus1 = plus1;
			this.minus1 = minus1;
			this.zero = zero;
		}

		/**
		 * @return the minus1
		 */
		public Mutation getMinus1() {
			return minus1;
		}

		/**
		 * @return the plus1
		 */
		public Mutation getPlus1() {
			return plus1;
		}

		/**
		 * @return the zero
		 */
		public Mutation getZero() {
			return zero;
		}

	}

	public static int mutationForLine = 0;

	static Logger logger = Logger.getLogger(RicMethodAdapter.class);

	public RicMethodAdapter(MethodVisitor mv, String className,
			String methodName) {
		super(mv, className.replace('/', '.'), methodName);
		logger.info("MethodName:" + methodName);

	}

	@Override
	public void visitInsn(int opcode) {
		if (mutationCode) {
			super.visitInsn(opcode);
			return;
		}
		switch (opcode) {
		case Opcodes.ICONST_M1:
			intConstant(-1);
			break;
		case Opcodes.ICONST_0:
			intConstant(0);
			break;
		case Opcodes.ICONST_1:
			intConstant(1);
			break;
		case Opcodes.ICONST_2:
			intConstant(2);
			break;
		case Opcodes.ICONST_3:
			intConstant(3);
			break;
		case Opcodes.ICONST_4:
			intConstant(4);
			break;
		case Opcodes.ICONST_5:
			intConstant(5);
			break;
		case Opcodes.LCONST_0:
			longConstant(0);
			break;
		case Opcodes.LCONST_1:
			longConstant(1);
			break;
		case Opcodes.FCONST_0:
			floatConstant(0);
			break;
		case Opcodes.FCONST_1:
			floatConstant(1);
			break;
		case Opcodes.FCONST_2:
			floatConstant(2);
			break;
		case Opcodes.DCONST_0:
			doubleConstant(0);
			break;
		case Opcodes.DCONST_1:
			doubleConstant(1);
			break;

		default:
			super.visitInsn(opcode);
			break;
		}
	}

	@Override
	public void visitLdcInsn(Object cst) {
		if (mutationCode) {
			super.visitLdcInsn(cst);
			return;
		}
		if (cst instanceof Integer) {
			Integer integerConstant = (Integer) cst;
			intConstant(integerConstant);
		} else if (cst instanceof Float) {
			Float floatConstant = (Float) cst;
			floatConstant(floatConstant);

		} else if (cst instanceof Long) {
			Long longConstant = (Long) cst;
			longConstant(longConstant);
		} else if (cst instanceof Double) {
			Double doubleConstant = (Double) cst;
			doubleConstant(doubleConstant);
		} else {
			mv.visitLdcInsn(cst);
		}
	}

	private void longConstant(final long longConstant) {
		logger.info("long constant for line: " + getLineNumber());
		ConstantMutations cm = getConstantMutations(className, getLineNumber(),
				mutationForLine);
		boolean insert = false;
		MutationCode unmutated = new MutationCode(null) {

			@Override
			public void insertCodeBlock(MethodVisitor mv) {
				mv.visitLdcInsn(new Long(longConstant));
			}

		};

		List<MutationCode> mutationCode = new ArrayList<MutationCode>();

		if (MutationManager.shouldApplyMutation(cm.getPlus1())) {
			insert = true;
			mutationCode.add(new MutationCode(cm.getPlus1()) {

				@Override
				public void insertCodeBlock(MethodVisitor mv) {
					mv.visitLdcInsn(new Long(longConstant + 1));
				}
			});
		}
		if (MutationManager.shouldApplyMutation(cm.getMinus1())) {
			insert = true;
			mutationCode.add(new MutationCode(cm.getMinus1()) {
				@Override
				public void insertCodeBlock(MethodVisitor mv) {
					mv.visitLdcInsn(new Long(longConstant - 1));
				}
			});
		}

		if (MutationManager.shouldApplyMutation(cm.getZero())) {
			insert = true;
			mutationCode.add(new MutationCode(cm.getZero()) {
				@Override
				public void insertCodeBlock(MethodVisitor mv) {
					mv.visitLdcInsn(new Long(0));
				}
			});
		}
		if (insert) {
			logger.info("Applying mutations for line: " + getLineNumber());
			BytecodeTasks.insertIfElse(mv, unmutated, mutationCode
					.toArray(new MutationCode[0]));
		} else {
			logger.info("Applying no mutation for line: " + getLineNumber());
			super.visitLdcInsn(new Long(longConstant));
		}
	}

	private void floatConstant(final float floatConstant) {
		logger.info("long constant for line: " + getLineNumber());
		ConstantMutations cm = getConstantMutations(className, getLineNumber(),
				mutationForLine);
		boolean insert = false;
		MutationCode unmutated = new MutationCode(null) {

			@Override
			public void insertCodeBlock(MethodVisitor mv) {
				mv.visitLdcInsn(new Float(floatConstant));
			}

		};

		List<MutationCode> mutationCode = new ArrayList<MutationCode>();

		if (MutationManager.shouldApplyMutation(cm.getPlus1())) {
			insert = true;
			mutationCode.add(new MutationCode(cm.getPlus1()) {

				@Override
				public void insertCodeBlock(MethodVisitor mv) {
					mv.visitLdcInsn(new Float(floatConstant + 1));
				}
			});
		}
		if (MutationManager.shouldApplyMutation(cm.getMinus1())) {
			insert = true;
			mutationCode.add(new MutationCode(cm.getMinus1()) {
				@Override
				public void insertCodeBlock(MethodVisitor mv) {
					mv.visitLdcInsn(new Float(floatConstant - 1));
				}
			});
		}

		if (MutationManager.shouldApplyMutation(cm.getZero())) {
			insert = true;
			mutationCode.add(new MutationCode(cm.getZero()) {
				@Override
				public void insertCodeBlock(MethodVisitor mv) {
					mv.visitLdcInsn(new Float(0));
				}
			});
		}
		if (insert) {
			logger.info("Applying mutations for line: " + getLineNumber());
			BytecodeTasks.insertIfElse(mv, unmutated, mutationCode
					.toArray(new MutationCode[0]));
		} else {
			logger.info("Applying no mutation for line: " + getLineNumber());
			super.visitLdcInsn(new Float(floatConstant));
		}
	}

	private void doubleConstant(final double doubleConstant) {
		logger.info("long constant for line: " + getLineNumber());
		ConstantMutations cm = getConstantMutations(className, getLineNumber(),
				mutationForLine);
		boolean insert = false;
		MutationCode unmutated = new MutationCode(null) {

			@Override
			public void insertCodeBlock(MethodVisitor mv) {
				mv.visitLdcInsn(new Double(doubleConstant));
			}

		};
		List<MutationCode> mutationCode = new ArrayList<MutationCode>();

		if (MutationManager.shouldApplyMutation(cm.getPlus1())) {
			insert = true;
			mutationCode.add(new MutationCode(cm.getPlus1()) {

				@Override
				public void insertCodeBlock(MethodVisitor mv) {
					mv.visitLdcInsn(new Double(doubleConstant + 1));
				}
			});
		}
		if (MutationManager.shouldApplyMutation(cm.getMinus1())) {
			insert = true;
			mutationCode.add(new MutationCode(cm.getMinus1()) {
				@Override
				public void insertCodeBlock(MethodVisitor mv) {
					mv.visitLdcInsn(new Double(doubleConstant - 1));
				}
			});
		}
		if (MutationManager.shouldApplyMutation(cm.getZero())) {
			insert = true;
			mutationCode.add(new MutationCode(cm.getZero()) {
				@Override
				public void insertCodeBlock(MethodVisitor mv) {
					mv.visitLdcInsn(new Double(0));
				}
			});
		}
		if (insert) {
			logger.info("Applying mutations for line: " + getLineNumber());
			BytecodeTasks.insertIfElse(mv, unmutated, mutationCode
					.toArray(new MutationCode[0]));
		} else {
			logger.info("Applying no mutation for line: " + getLineNumber());
			super.visitLdcInsn(new Double(doubleConstant));
		}
	}


	private void intConstant(final int intConstant) {
		logger.info("long constant for line: " + getLineNumber());
		ConstantMutations cm = getConstantMutations(className, getLineNumber(),
				mutationForLine);
		boolean insert = false;
		MutationCode unmutated = new MutationCode(null) {

			@Override
			public void insertCodeBlock(MethodVisitor mv) {
				mv.visitLdcInsn(new Integer(intConstant));
			}

		};

		List<MutationCode> mutationCode = new ArrayList<MutationCode>();

		if (MutationManager.shouldApplyMutation(cm.getPlus1())) {
			insert = true;
			mutationCode.add(new MutationCode(cm.getPlus1()) {

				@Override
				public void insertCodeBlock(MethodVisitor mv) {
					mv.visitLdcInsn(new Integer(intConstant + 1));
				}
			});
		}
		if (MutationManager.shouldApplyMutation(cm.getMinus1())) {
			insert = true;
			mutationCode.add(new MutationCode(cm.getMinus1()) {
				@Override
				public void insertCodeBlock(MethodVisitor mv) {
					mv.visitLdcInsn(new Integer(intConstant - 1));
				}
			});
		}

		if (MutationManager.shouldApplyMutation(cm.getZero())) {
			insert = true;
			mutationCode.add(new MutationCode(cm.getZero()) {
				@Override
				public void insertCodeBlock(MethodVisitor mv) {
					mv.visitLdcInsn(new Integer(0));
				}
			});
		}
		if (insert) {
			logger.info("Applying mutations for line: " + getLineNumber());
			BytecodeTasks.insertIfElse(mv, unmutated, mutationCode
					.toArray(new MutationCode[0]));
		} else {
			logger.info("Applying no mutation for line: " + getLineNumber());
			super.visitLdcInsn(new Integer(intConstant));
		}
	}


	// private void insertPrintStatements(String message) {
	// insertPrintStatements(mv, message);
	// }

	@Override
	public void visitLineNumber(int line, Label start) {
		super.visitLineNumber(line, start);
		mutationForLine = 0;
	}

	@Override
	public void visitIntInsn(int opcode, int operand) {
		if (mutationCode) {
			super.visitIntInsn(opcode, operand);
			return;
		}
		if (opcode == Opcodes.BIPUSH || opcode == Opcodes.SIPUSH) {
			intConstant(operand);
		} else {
			super.visitIntInsn(opcode, operand);
		}
	}


	private static ConstantMutations getConstantMutations(String className,
			int lineNumber, int mutationForLine) {
		Mutation mutationPlus1 = new Mutation(className, lineNumber,
				mutationForLine, MutationType.RIC_PLUS_1);
		Mutation mutationMinus = new Mutation(className, lineNumber,
				mutationForLine, MutationType.RIC_MINUS_1);
		Mutation mutationZero = new Mutation(className, lineNumber,
				mutationForLine, MutationType.RIC_ZERO);
		Mutation mutationPlus1FromDB = QueryManager.getMutation(mutationPlus1);
		Mutation mutationMinus1FromDB = QueryManager.getMutation(mutationMinus);
		Mutation mutationZeroFromDB = QueryManager.getMutation(mutationZero);
		return new ConstantMutations(mutationPlus1FromDB, mutationMinus1FromDB,
				mutationZeroFromDB);

	}
}
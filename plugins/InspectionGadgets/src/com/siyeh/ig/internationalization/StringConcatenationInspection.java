package com.siyeh.ig.internationalization;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.psi.*;
import com.siyeh.ig.BaseInspection;
import com.siyeh.ig.BaseInspectionVisitor;
import com.siyeh.ig.ExpressionInspection;
import com.siyeh.ig.GroupNames;
import com.siyeh.ig.psiutils.TypeUtils;

public class StringConcatenationInspection extends ExpressionInspection {

    public String getDisplayName() {
        return "String concatenation";
    }

    public String getGroupDisplayName() {
        return GroupNames.INTERNATIONALIZATION_GROUP_NAME;
    }

    public String buildErrorString(PsiElement location) {
        return "String concatenation (#ref) in an internationalized context #loc";
    }

    public BaseInspectionVisitor createVisitor(InspectionManager inspectionManager, boolean onTheFly) {
        return new StringConcatenationVisitor(this, inspectionManager, onTheFly);
    }

    private static class StringConcatenationVisitor extends BaseInspectionVisitor {
        private StringConcatenationVisitor(BaseInspection inspection, InspectionManager inspectionManager, boolean isOnTheFly) {
            super(inspection, inspectionManager, isOnTheFly);
        }

        public void visitBinaryExpression(PsiBinaryExpression expression) {
            super.visitBinaryExpression(expression);
            final PsiJavaToken sign = expression.getOperationSign();
            if (sign == null) {
                return;
            }
            if (!(sign.getTokenType() == JavaTokenType.PLUS)) {
                return;
            }
            final PsiExpression lhs = expression.getLOperand();
            if (lhs == null) {
                return;
            }
            final PsiType lhsType = lhs.getType();
            if (TypeUtils.isJavaLangString(lhsType)) {
                registerError(sign);
                return;
            }

            final PsiExpression rhs = expression.getROperand();
            if (rhs == null) {
                return;
            }
            final PsiType rhsType = rhs.getType();
            if (TypeUtils.isJavaLangString(rhsType)) {
                registerError(sign);
                return;
            }

        }

    }

}

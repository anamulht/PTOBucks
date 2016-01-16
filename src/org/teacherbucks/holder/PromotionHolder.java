package org.teacherbucks.holder;

import java.util.Vector;

import org.teacherbucks.model.Promotion;

public class PromotionHolder {
	
	public static Vector<Promotion> promotionModels = new Vector<Promotion>();
	
	public static Vector<Promotion> getAllPromotionList(){
		return promotionModels;
	}
	
	public static void setAllPromotionsList(Vector<Promotion> promotionModels){
		PromotionHolder.promotionModels = promotionModels;
	}
	
	public static Promotion getPromotion(int pos){
		return promotionModels.elementAt(pos);
	}
	
	public static void setPromotionList(Promotion promotion){
		PromotionHolder.promotionModels.addElement(promotion);
	}
	
	public static void removePromotionList(){
		PromotionHolder.promotionModels.removeAllElements();
	}

}

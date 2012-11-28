package dustmod.runes;

import java.util.List;

import net.minecraft.src.EntityItem;
import net.minecraft.src.ItemStack;
import dustmod.DustEvent;
import dustmod.DustMod;
import dustmod.EntityDust;
import dustmod.InscriptionEvent;
import dustmod.InscriptionManager;

public class DEChargeInscription extends DustEvent {
	
	
	@Override
	protected void onInit(EntityDust e) {
		super.onInit(e);
        e.setRenderBeam(true);
        e.setRenderStar(true);
        e.setColorStarOuter(0,0,255);
        e.setColorBeam(0,0,255);
	}
	
	@Override
	protected void onTick(EntityDust e) {
		super.onTick(e);
        e.setStarScale(e.starScale + 0.005F);
        if (e.ticksExisted > 20)
        {
        	ItemStack inscription;
        	List<EntityItem> items = this.getItems(e);
        	
        	for(EntityItem ei:items){
        		ItemStack i = ei.item;
        		if(i.itemID == DustMod.wornInscription.shiftedIndex){
        			InscriptionEvent evt = InscriptionManager.getEvent(i);
        			if(evt != null){
        				boolean sucess = evt.callSacrifice(this,e, i);
        				if(sucess) {
        					e.fade();
        					return;
        				}
        			}
        		}
        	}
        	e.fizzle();
        	return;
        }
	}
}

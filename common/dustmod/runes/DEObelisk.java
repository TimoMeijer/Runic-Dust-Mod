/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import java.util.List;

import dustmod.DustEvent;
import dustmod.EntityDust;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.BlockFluid;
import net.minecraft.src.Entity;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

/**
 *
 * @author billythegoat101
 */
public class DEObelisk extends DustEvent
{
    public static final int ticksperblock = 20;

    public void onInit(EntityDust e)
    {
        //e.ignoreRune = true;
//        int compare = mod_DustMod.compareDust(mod_DustMod.plantDID, e.dustID);
//        if(compare != 0){
//            e.fizzle();
//            return;
//        }
//
        e.renderBeam = true;
        e.setColorBeam(114, 53, 62);
        ItemStack[] sacrifice = new ItemStack[1];
        sacrifice[0] = new ItemStack(Block.oreIron, 2);
        this.sacrifice(e, sacrifice);

        if (sacrifice[0].stackSize != 0)
        {
            e.fizzle();
            return;
        }

        e.data[1] = 1;
    }

    public void onTick(EntityDust e)
    {
//        if(e.ri != 0){
//            e.data[1] = e.ri;
//            e.ri = 0;
//        }
        
        if (e.ticksExisted < ticksperblock * 2)
        {
            return;
        }

        int height = 16;
        World world = e.worldObj;
        int x = (int)e.getX();
        int y = (int)e.getY();
        int z = (int)e.getZ();

        if (e.ticksExisted % ticksperblock == 0 && (e.data[0] < height))
        {
            if (e.data[1] > 0)
            {
                List<Entity> ents = getEntities(e.worldObj, (double)x + 0.5D, (double)y + (double)e.data[0] + 2D, (double)z + 0.5D, 1D);

                for (Entity i: ents)
                {
                    i.setPosition((double)x + 0.5D, (double)y + (double)e.data[0] + 3D, (double)z + 0.5D);
                }
            }

            if (e.data[1] == 1)
            {
                for (int t = -8; t < height; t++)
                {
                    int c = -t + e.data[0] - 1;

                    if (y + c <= 0)
                    {
                        e.fade();
                        return;
                    }

                    int b = world.getBlockId(x, y + c, z);
                    int m = world.getBlockMetadata(x, y + c, z);
                    int nb = world.getBlockId(x, y + c + 1, z);
                    Block B = Block.blocksList[b];

                    if ((b == 0 || (B != null && B instanceof BlockFluid)) && world.getBlockId(x, y + c + 2, z) != 0)
                    {
                        b = Block.cobblestone.blockID;
                        B = Block.cobblestone;
                    }

                    Block nB = Block.blocksList[nb];

                    if ((B != null && B instanceof BlockContainer) && (nB != null && !(nB instanceof BlockContainer)))
                    {
                        e.fade();
                        return;
                    }

                    world.setBlockAndMetadataWithNotify(x, y + c + 1, z, b, m);
                    world.setBlockWithNotify(x, y + c, z, 0);
                }
            }
            else
            {
                for (int t = height; t >= -9; t--)
                {
                    if (y - t + e.data[0] <= 0)
                    {
                        e.fade();
                        return;
                    }

                    int b = world.getBlockId(x, y - t + e.data[0], z);
                    int m = world.getBlockMetadata(x, y - t + e.data[0], z);
                    int nb = world.getBlockId(x, y - t + e.data[0] + e.data[1], z);
                    Block B = Block.blocksList[b];
                    Block nB = Block.blocksList[nb];

                    if ((B != null && B instanceof BlockContainer) || (nB != null && nB instanceof BlockContainer))
                    {
                        e.fade();
                        return;
                    }

                    world.setBlockAndMetadataWithNotify(x, y - t + e.data[0] + e.data[1], z, b, m);
                    world.setBlockWithNotify(x, y - t + e.data[0], z, 0);
                }
            }

            e.data[0] += e.data[1];
        }

        if (e.data[0] >= height && world.getBlockId(x, y + height - 1, z) == 0)
        {
            e.data[1] = -1;
            e.data[0]--;
        }

        if (e.data[0] < 0)
        {
            e.fade();
        }

        if (e.ticksExisted - ticksperblock * (height + 2) > 36000 && e.data[1] > 0)
        {
            e.fade();
        }
    }
}

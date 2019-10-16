package shape;

import java.awt.Color;
import java.util.Random;

/**
 * @author Benzolamps
 */
public class Trix {
    public static final Trix[] L_BLOCK = new Trix[]{
            new Trix(Color.RED, 5, 9, 13, 14),
            new Trix(Color.RED, 9, 10, 11, 13),
            new Trix(Color.RED, 5, 6, 10, 14),
            new Trix(Color.RED, 11, 13, 14, 15)
    };

    public static final Trix[] REVERSE_L_BLOCK = new Trix[]{
            new Trix(Color.BLUE, 6, 10, 13, 14),
            new Trix(Color.BLUE, 9, 13, 14, 15),
            new Trix(Color.BLUE, 5, 6, 9, 13),
            new Trix(Color.BLUE, 9, 10, 11, 15)
    };

    public static final Trix[] SQUARE = new Trix[]{
            new Trix(Color.ORANGE, 9, 10, 13, 14)
    };

    public static final Trix[] T_BLOCK = new Trix[]{
            new Trix(Color.MAGENTA, 10, 13, 14, 15),
            new Trix(Color.MAGENTA, 5, 9, 10, 13),
            new Trix(Color.MAGENTA, 9, 10, 11, 14),
            new Trix(Color.MAGENTA, 6, 9, 10, 14)
    };

    public static final Trix[] SWAGERLY = new Trix[]{
            new Trix(Color.CYAN, 9, 10, 14, 15),
            new Trix(Color.CYAN, 6, 9, 10, 13)
    };

    public static final Trix[] REVERSE_SWAGERLY = new Trix[]{
            new Trix(Color.GRAY, 10, 11, 13, 14),
            new Trix(Color.GRAY, 5, 9, 10, 14)
    };

    public static final Trix[] LINE_PIECE = new Trix[]{
            new Trix(Color.PINK, 12, 13, 14, 15),
            new Trix(Color.PINK, 2, 6, 10, 14)
    };

    public static int lBlockCount = 0;
    public static int reverseLBlockCount = 0;
    public static int squareCount = 0;
    public static int tBlockCount = 0;
    public static int swagerlyCount = 0;
    public static int reverseSwagerlyCount = 0;
    public static int linePieceCount = 0;

    private int[] loc = new int[4];
    private Block[] block = new Block[4];

    public Trix(Color color, int i, int j, int k, int l) {
        loc[0] = i;
        loc[1] = j;
        loc[2] = k;
        loc[3] = l;
        block[0] = new Block(6 + (i % 4), i / 4 - 2, color);
        block[1] = new Block(6 + (j % 4), j / 4 - 2, color);
        block[2] = new Block(6 + (k % 4), k / 4 - 2, color);
        block[3] = new Block(6 + (l % 4), l / 4 - 2, color);
        System.out.println(i / 4 - 2);
        System.out.println(j / 4 - 2);
        System.out.println(k / 4 - 2);
        System.out.println(l / 4 - 2);
    }

    public Trix() {
        loc = SQUARE[0].loc.clone();
        for (int i = 0; i < 4; i++) {
            block[i] = (Block) SQUARE[0].block[i].clone();
        }
    }

    public Block[] getBlock() {
        return block;
    }

    public void setBlock(Block[] block) {
        this.block = block;
    }

    public static Trix[] getAllTrix() {
        Trix[] t = new Trix[]{
                Trix.L_BLOCK[0].clone(), Trix.REVERSE_L_BLOCK[0].clone(), Trix.SQUARE[0].clone(),
                Trix.T_BLOCK[0].clone(), Trix.SWAGERLY[0].clone(), Trix.REVERSE_SWAGERLY[0].clone(),
                Trix.LINE_PIECE[0].clone()
        };
        return t;
    }

    public static int[] getAllCount() {
        int[] c = new int[]{
                Trix.lBlockCount, Trix.reverseLBlockCount, Trix.squareCount, Trix.tBlockCount,
                Trix.swagerlyCount, Trix.reverseSwagerlyCount, Trix.linePieceCount
        };
        return c;
    }

    public static void countToZero() {
        Trix.lBlockCount = Trix.reverseLBlockCount = Trix.squareCount
                = Trix.tBlockCount = Trix.swagerlyCount = Trix.reverseSwagerlyCount
                = Trix.linePieceCount = 0;
    }

    public static Trix ProduceOneTrix() {
        Random r = new Random();
        switch (r.nextInt(7)) {
            case 0:
                lBlockCount++;
                return L_BLOCK[r.nextInt(L_BLOCK.length)].clone();
            case 1:
                reverseLBlockCount++;
                return REVERSE_L_BLOCK[r.nextInt(REVERSE_L_BLOCK.length)].clone();
            case 2:
                squareCount++;
                return SQUARE[r.nextInt(SQUARE.length)].clone();
            case 3:
                tBlockCount++;
                return T_BLOCK[r.nextInt(T_BLOCK.length)].clone();
            case 4:
                swagerlyCount++;
                return SWAGERLY[r.nextInt(SWAGERLY.length)].clone();
            case 5:
                reverseSwagerlyCount++;
                return REVERSE_SWAGERLY[r.nextInt(REVERSE_SWAGERLY.length)].clone();
            default:
                linePieceCount++;
                return LINE_PIECE[r.nextInt(LINE_PIECE.length)].clone();
        }
    }

    public Trix clone() {
        Trix trix = new Trix();
        for (int i = 0; i < 4; i++) {
            trix.block[i] = (Block) block[i].clone();
        }
        trix.loc = loc.clone();
        return trix;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
			return true;
		}
        if (other == null) {
			return false;
		}
        if (getClass() != other.getClass()) {
			return false;
		}
        for (int i = 0; i < 4; i++) {
			if (loc[i] != ((Trix) other).loc[i]) {
				return false;
			}
		}
        return true;
    }

    public Trix rotate() {
        Trix t = new Trix();
        for (int i = 0; i < 4; i++) {
            if (equals(L_BLOCK[i])) {
				t = L_BLOCK[(i + 1) % 4].clone();
			}

            if (equals(REVERSE_L_BLOCK[i])) {
				t = REVERSE_L_BLOCK[(i + 1) % 4].clone();
			}

            if (equals(T_BLOCK[i])) {
				t = T_BLOCK[(i + 1) % 4].clone();
			}
        }

        for (int i = 0; i < 2; i++) {
            if (equals(SWAGERLY[i])) {
				t = SWAGERLY[(i + 1) % 2].clone();
			}

            if (equals(REVERSE_SWAGERLY[i])) {
				t = REVERSE_SWAGERLY[(i + 1) % 2].clone();
			}

            if (equals(LINE_PIECE[i])) {
				t = LINE_PIECE[(i + 1) % 2].clone();
			}
        }

        for (int i = 0; i < 4; i++) {
            int m = block[i].getM() - loc[i] % 4;
            int n = block[i].getN() - loc[i] / 4;
            t.getBlock()[i].setM(m + t.loc[i] % 4);
            t.getBlock()[i].setN(n + t.loc[i] / 4);
        }

        return t;
    }
}

package io.ignice.c17n.gfx;

//http://giflib.sourceforge.net/whatsinagif/bits_and_bytes.html
public class GIF {

    // TODO WARNING: NEED TO SWAP TO LITTLE ENDIAN ORDER
    // TODO WARNING: NEED TO SWAP TO LITTLE ENDIAN ORDER
    // TODO WARNING: NEED TO SWAP TO LITTLE ENDIAN ORDER
    // TODO WARNING: NEED TO SWAP TO LITTLE ENDIAN ORDER
    // TODO WARNING: NEED TO SWAP TO LITTLE ENDIAN ORDER

    //
    // Anatomy of a GIF:
    // [source: http://ntfs.com/gif-signature-format.htm]

//    interface GifDescriptor {
//        enum Header { GIF87A, GIF89A }
//        Header header();
//        void header(Header header);
//        //
//        enum LogicalScreenDescriptor {
//            DIMENSION(),
//            PACKED_FIELD(),
//            BACKGROUND_COLOR_INDEX(),
//            PIXEL_ASPECT_RATIO();
//
////            private final long MSB_MASK;
////            private final long LSB_MASK;
////
////            LogicalScreenDescriptor(long msbMask, long lsbMask) {
////                MSB_MASK = msbMask;
////                LSB_MASK = lsbMask;
////            }
//
//            enum Dimension {
//                CANVAS_WIDTH(0xFFFF0000),
//                CANVAS_HEIGHT(0x0000FFFF);
//                private final long MASK;
//                Dimension(long mask) {
//                    MASK = mask;
//                }
//            }
//
//            enum PackedField {
//                GLOBAL_COLOR_TABLE_FLAG(   u8(0b10000000)),
//                COLOR_RESOLUTION(          u8(0b01110000)),
//                SORT_FLAG(                 u8(0b00001000)),
//                SIZE_OF_GLOBAL_COLOR_TABLE(u8(0b00000111));
//
//                private final byte MASK;
//
//                PackedField(byte mask) {
//                    MASK = mask;
//                }
//            }
//        }
//
//
//
//        record ScreenDescriptor(int width, int height, PackedField packedField, int backgroundColorIndex, int pixelAspectRatio) {}
//        ScreenDescriptor screenDescriptor();
//        void screenDescriptor(ScreenDescriptor screenDescriptor);
//    }

    // HEADER
    // (6 bytes, fixed length)
    // HEADER = "GIF87a" or "GIF89a"
    //  "GIF" = 0x47494638
    //   "7a" = 0x3761
    //   "9a" = 0x3961

    // SCREEN DESCRIPTOR
    // (4 bytes, fixed length)
    // SCREEN DESCRIPTOR = WIDTH HEIGHT
    //             WIDTH ∈ [0x0000, 0xFFFF] (2 bytes)
    //            HEIGHT ∈ [0x0000, 0xFFFF] (2 bytes)

    // (1 byte or 1+GCT_SIZE bytes, optional, N)
    // GLOBAL_COLOR_TABLE
    // (1 or 1+N bytes, optional)

    // GLOBAL_COLOR_TABLE = FLAGS MAYBE[DATA]
    //              FLAGS ∈ [0b00000000, 0b11111111]
    //           IS_EMPTY = (FLAGS & 0b10000000) == 0 // MSB indicates if GCT exists
    //       GCT_SIZE ≡ N = if IS_EMPTY then 0 else (3 * (1 <<((FLAGS & 0x07) + 1)))
    //  DATA_BLOCK_OFFSET = GCT_SIZE + 13 (note: 13 is the GIF header size)
    //    note: DATA_BLOCK_OFFSET is the offset to the FIRST data block. Data blocks continue until "!" literal.

    // note: generally speaking:
    //  IMAGE_BLOCK = (1 byte ",") (11 byte header, fixed, the)

    // BEGIN_IMAGE_BLOCK (MAGIC_NUMBER ",")
    // (1 byte)
    // "," = 0x2C
    // prefixes IMAGE (which itself is IMAGE_DESCRIPTOR optionally followed by Color Table then values)

    // IMAGE_HEADER (aka IMAGE_DESCRIPTOR)
    // (11 bytes, fixed length)
    // IMAGE_HEADER = 11 bytes where the last byte is a GCT flag
    // todo http://ntfs.com/gif-signature-format.htm

    // TODO WARNING: IN EXAMPLE A SUB-BLOCK APPEARS HERE?

    // LOCAL_COLOR_TABLE
    // (? bytes, optional)

    // IMAGE_DATA
    // (todo bytes variable?)
    // IMAGE_DATA = BIT_WIDTH LINKED_LIST_OF_LZW_SUB_BLOCKS
    //  BIT_WIDTH ∈ [0x02, 0xFF] (1 byte)
    //  BIT_WIDTH gives the bit-width of the unencoded symbols
    //  note that BIT_WIDTH MUST be greater than or equal to 2 (even for bi-color images))

    // BEGIN_EXTENSION_BLOCK (MAGIC_NUMBER)
    // (1 byte)
    // "!" = 0x21
    // prefixes EXTENSION_BLOCK

    // EXTENSION_BLOCK
    // (8 bytes, fixed)
    // todo http://ntfs.com/gif-signature-format.htm

    // TRAILER (MAGIC_NUMBER)
    // (1 byte)
    // ";" = 0x3B
    // last byte in the file

//    private final ByteBuffer buffer = ByteBuffer.allocate().order(ByteOrder.LITTLE_ENDIAN)

}

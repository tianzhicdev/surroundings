package org.surroundings;
import org.bitcoinj.core.*;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.MemoryBlockStore;
import org.bitcoinj.utils.BlockFileLoader;

import java.io.File;
import java.util.Arrays;

public class BlockchainReader {
    public static void main(String[] args) {
        try {
            // Set the Bitcoin network parameters (MainNet in this case)
            NetworkParameters params = MainNetParams.get();

            // Path to the Bitcoin Core block storage directory
            File blocksDir = new File("/Users/biubiu/projects/surroundings/btccore_reader/"); // Replace with your actual path

            // Create a memory-based block store (not persistent)
            BlockStore blockStore = new MemoryBlockStore(params);
            BlockChain chain = new BlockChain(params, blockStore);

            // Load blocks from the blkNNNNN.dat files in the blocks directory
            BlockFileLoader loader = new BlockFileLoader(params, Arrays.stream(blocksDir.listFiles()).toList());

            // Iterate through each block in the blockchain
            for (Block block : loader) {
                System.out.println("========================================");
                System.out.println("Block Hash: " + block.getHashAsString());
                System.out.println("Block Time: " + block.getTime());
                System.out.println("Transaction Count: " + block.getTransactions().size());

                // Iterate through each transaction in the block
                for (Transaction tx : block.getTransactions()) {
                    System.out.println("\n  Transaction ID: " + tx.getTxId());
                    System.out.println("  Inputs:");
                    for (TransactionInput input : tx.getInputs()) {
                        try {
                            TransactionOutput connectedOutput = input.getOutpoint().getConnectedOutput();
                            if (connectedOutput != null) {
                                Address fromAddress = connectedOutput.getScriptPubKey().getToAddress(params);
                                System.out.println("    From Address: " + (fromAddress != null ? fromAddress.toString() : "Unknown"));
                            }
                        } catch (Exception e) {
                            System.out.println("    Unable to decode input address.");
                        }
                    }

                    System.out.println("  Outputs:");
                    for (TransactionOutput output : tx.getOutputs()) {
                        try {
                            Address toAddress = output.getScriptPubKey().getToAddress(params);
                            System.out.println("    To Address: " + (toAddress != null ? toAddress.toString() : "Unknown"));
                            System.out.println("    Amount: " + output.getValue().toPlainString() + " BTC");
                        } catch (Exception e) {
                            System.out.println("    Unable to decode output address.");
                        }
                    }
                }
                System.out.println("========================================\n");

                // Uncomment this line if you want to limit how many blocks are processed
                // break; // Process only the first block for testing
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

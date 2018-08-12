package me.modmuss50.ftbeggs.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class ClassTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
	    //Adds some ambient lighting
        if (transformedName.equals("net.minecraft.world.WorldProvider") || transformedName.equals("ayk")) {
            System.out.println("Found WorldProvider");
            ClassNode classNode = readClassFromBytes(basicClass);
            for (MethodNode methodNode : classNode.methods) {
                if (methodNode.name.equals("generateLightBrightnessTable") || methodNode.name.equals("func_76556_a") || (methodNode.name.equals("a") && methodNode.desc.equals("()V"))) {
                    System.out.println("found generateLightBrightnessTable");
                    boolean foundLDCIns = false;
                    boolean foundFirstFCONST_1 = false;
                    for (AbstractInsnNode insnNode : methodNode.instructions.toArray()) {
                        if (insnNode instanceof LdcInsnNode) {
                            LdcInsnNode ldcInsnNode = (LdcInsnNode) insnNode;
                            if (ldcInsnNode.cst instanceof Float) {
                                if (ldcInsnNode.cst.equals(3.0f)) {
                                    foundLDCIns = true;
                                }
                            }
                        }
                        if (foundLDCIns) {
                            if (insnNode.getOpcode() == Opcodes.FCONST_0) {
                                methodNode.instructions.set(insnNode, new LdcInsnNode(new Float(0.1)));
                            }
                            if (foundFirstFCONST_1 && insnNode.getOpcode() == Opcodes.FCONST_1) {
                                methodNode.instructions.set(insnNode, new LdcInsnNode(new Float(0.9)));
                            }
                            if (insnNode.getOpcode() == Opcodes.FCONST_1) {
                                foundFirstFCONST_1 = true;
                            }
                        }
                    }
                }
            }
            return writeClassToBytes(classNode);
        }
        if(transformedName.equals("net.minecraft.client.gui.GuiMainMenu") || transformedName.equals("blp")){
	        System.out.println("Found GuiMainMenu");
            ClassNode classNode = readClassFromBytes(basicClass);
            String drawString = LoadingCore.runtimeDeobfuscationEnabled ? "func_73863_a" : "drawScreen";
            classNode.methods.stream()
                    .filter(methodNode -> methodNode.name.equals(drawString) || (methodNode.name.equals("a") && methodNode.desc.equals("(IIF)V")))
                    .forEach(methodNode -> {
	                    System.out.println("Found drawScreen");
                        for(AbstractInsnNode node : methodNode.instructions.toArray()){
                        	if(node instanceof MethodInsnNode){
                        		MethodInsnNode methodInsnNode = (MethodInsnNode) node;
		                        if(methodInsnNode.name.equals(drawString) || (methodInsnNode.name.equals("a") && methodInsnNode.desc.equals("(IIF)V"))){
			                        System.out.println("Added early return");
			                        methodNode.instructions.insert(methodInsnNode, new InsnNode(Opcodes.RETURN));
		                        }
	                        }

                        }
                    });
	        return writeClassToBytes(classNode);
        }
        return basicClass;
    }

    public static ClassNode readClassFromBytes(byte[] bytes) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        return classNode;
    }

    public static byte[] writeClassToBytes(ClassNode classNode) {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }
}

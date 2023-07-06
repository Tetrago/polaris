package tetrago.polaris.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import tetrago.polaris.module.Mod

class Processor(
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val map = resolver.getSymbolsWithAnnotation(Mod::class.qualifiedName!!).associateWith { it.validate() }

        map.filter { it.value }.keys
            .filterIsInstance<KSClassDeclaration>()
            .forEach { it.accept(ModuleVisitor(logger, codeGenerator), Unit) }

        return map.filterNot { it.value }.keys.toList()
    }
}

class ProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return Processor(environment.logger, environment.codeGenerator)
    }
}

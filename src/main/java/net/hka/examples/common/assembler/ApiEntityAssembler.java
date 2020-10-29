package net.hka.examples.common.assembler;

import org.springframework.hateoas.RepresentationModel;

import net.hka.examples.common.domain.BaseEntity;

/**
 * Interface for components that convert a domain entity type into a domain DTO type and vice verse.
 *
 * @author Hany Kamal
 */
public interface ApiEntityAssembler<T extends BaseEntity, D extends RepresentationModel<D>> {
	/**
	 * Converts the {@code D} into a {@code T}.
	 *
	 * @param data transfer object (DTO) 
	 * @return
	 */
	T toEntity(D dto);
}
